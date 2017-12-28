package edu.nf.beans.handler;

import edu.nf.beans.BeanFactory;
import edu.nf.beans.InjectHandler;
import edu.nf.beans.annotations.Inject;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * Set方法注入器
 */
public class MethodInjectHandler implements InjectHandler {

    /**
     * 使用内省机制
     * @param target
     * @param targetClass
     * @param factory
     */
    public void handle(Object target, Class<?> targetClass, BeanFactory factory) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(targetClass,
                    Object.class);
            PropertyDescriptor[] propertyDescriptors = beanInfo
                    .getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                targetClass.getDeclaredField(propertyDescriptor.getName());
                Method setMethod = propertyDescriptor.getWriteMethod();
                if (setMethod != null && setMethod.isAnnotationPresent(Inject.class)) {
                    // 获取该方法上的Inject注解
                    Inject annotation = setMethod.getAnnotation(Inject.class);
                    // 根据注解name属性的值,从容器获取bean实例
                    Object property = factory.getBean(annotation.name());
                    // 回调set方法将property注入
                    setMethod.invoke(target, property);
                }
            }
        } catch (Exception e) {
            new RuntimeException("Set method inject fail.", e);
        }
    }
}
