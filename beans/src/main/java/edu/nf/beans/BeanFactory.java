package edu.nf.beans;

import edu.nf.beans.annotations.Component;
import edu.nf.beans.annotations.Scope;
import edu.nf.beans.utils.BeanNameUtil;
import edu.nf.beans.utils.ScanUtil;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {
    /**
     * 存放bean的描述
     */
    final Map<String, BeanDefinition> definitionMap = new ConcurrentHashMap<>();

    /**
     * 存放单例bean的实例
     */
    final Map<String, Object> singletonMap = new ConcurrentHashMap<>();

    /**
     * 在构造方法中初始化并构建所有bean描述
     * 以及单例的bean
     *
     * @param path 扫描路径
     */
    public BeanFactory(String path) {
        Set<String> classNames = ScanUtil.scan(path);
        //初始化原型
        initDefinitionMap(classNames);
        //初始化单例
        initSingleton();
        //执行singleton实例装配
        assemblySingletons();
    }

    /**
     * 根据扫描的类名进行解析，找出带有@Component注解的类，并构建成
     * BeanDefinition实例，保存到definitionMap集合中
     */
    private void initDefinitionMap(Set<String> classNames) {
        for (String className : classNames) {
            Class<?> beanClass = getClass(className);
            //检查beanClass是否标注了@Component注解
            if (beanClass.isAnnotationPresent(Component.class)) {
                //获取@Component注解的value属性的值，这个值作为bean在容器的唯一标识
                String beanName = beanClass.getAnnotation(Component.class).value();
                //如果没有执行value，默认将类名作为beanName，并将类名首字母变为小写
                beanName = "".equals(beanName) ? BeanNameUtil.toLowerBeanName(beanClass.getSimpleName()) : beanName;
                //如果容器已经存在bean，则抛出异常
                if (definitionMap.containsKey(beanName)) {
                    throw new RuntimeException(
                            "conflicts with existing, non-compatible bean definition of same name and class ["
                                    + beanClass + "]");
                } else {
                    definitionMap.put(beanName,
                            createBeanDefinition(beanClass));
                }
            }
        }
    }

    /**
     * 根据权限顶类名获取Class对象
     *
     * @param className
     * @return
     */
    private Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can not find the class name " + className + " to build the description.");
        }
    }

    /**
     * 构建bean描述定义,将bean的scope以及类名封装到BeanDefinition中
     * 创建的Bean描述会放入definitionMap的集合中保存
     * Bean的类名作为集合的key,而整个BeanDefinition对象作为value
     *
     * @param beanClass
     */
    private BeanDefinition createBeanDefinition(Class<?> beanClass) {
        // 创建BeanDefinition
        BeanDefinition definition = new BeanDefinition();
        //设置Bean的Class对象
        definition.setBeanClass(beanClass);
        //设置Bean的作用域
        definition.setScope(resolveScope(beanClass));
        return definition;
    }

    /**
     * 解析Scope，如果bean的class上指定了Scope注解,则将@Scope的value属性值作为Bean的创建方式
     * 否则Bean的默认创建方式将使用单例
     */
    private String resolveScope(Class<?> beanClass) {
        String scope = (beanClass.isAnnotationPresent(Scope.class)) ? beanClass
                .getAnnotation(Scope.class).value() : "singleton";
        return scope;
    }

    /**
     * 初始化SINGLETON实例放入bean容器中
     */
    private void initSingleton() {
        for (String beanName : definitionMap.keySet()) {
            BeanDefinition definition = definitionMap.get(beanName);
            if ("singleton".equals(definition.getScope())) {
                Object bean = newInstance(definition);
                singletonMap.put(beanName, bean);
            }
        }
    }

    /**
     * 为所有singleton实例执行装配（依赖注入）
     */
    private void assemblySingletons() {
        for (String beanName : singletonMap.keySet()) {
            Class<?> beanClass = definitionMap.get(beanName).getBeanClass();
            Object bean = singletonMap.get(beanName);
            InjectHandlerInvoker.inject(bean, beanClass, this);
        }
    }

    /**
     * 为prototype实例执行装配
     */
    protected Object assemblyPrototype(BeanDefinition definition){
        Object bean = newInstance(definition);
        InjectHandlerInvoker.inject(bean, definition.getBeanClass(), this);
        return bean;
    }

    /**
     * 根据描述定义创建Bean实例
     * @param definition
     * @return
     */
    private Object newInstance(BeanDefinition definition) {
        try {
            return definition.getBeanClass().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Create bean instance fail.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Create bean instance fail.", e);
        }
    }

    /**
     * 获取bean实例
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        return doGetBean(beanName);
    }

    /**
     * 获取bean实例(泛型)
     *
     * @param beanName
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String beanName, Class<T> clazz) {
        return (T) doGetBean(beanName);
    }

    /**
     * 从容器中获取Bean的BeanDefinition
     * 如果Bean的BeanDefinition的scope为singleton,则从singletonMap中获取单例
     * 否则以原型的方式创建并返回
     */
    private Object doGetBean(String beanName) {
        BeanDefinition definition = definitionMap.get(beanName);
        if("singleton".equals(definition.getScope())){
            return singletonMap.get(beanName);
        }
        return assemblyPrototype(definition);
    }

    public void close(){
        // 清空Bean描述集合
        definitionMap.clear();
        // 清空Bean实例集合
        singletonMap.clear();
    }
}
