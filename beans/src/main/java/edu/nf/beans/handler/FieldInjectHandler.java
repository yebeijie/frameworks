package edu.nf.beans.handler;

import edu.nf.beans.BeanFactory;
import edu.nf.beans.InjectHandler;
import edu.nf.beans.annotations.Inject;
import java.lang.reflect.Field;

/**
 * 字段注入器
 */
public class FieldInjectHandler implements InjectHandler {

    public void handle(Object target, Class<?> targetClass, BeanFactory factory) {
        // 遍历当前类中字段
        for (Field field : targetClass.getDeclaredFields()) {
            // 判断字段中是否定义了Inject注解类型
            if (field.isAnnotationPresent(Inject.class)) {
                // 获取该字段上的Inject注解
                Inject annotation = field.getAnnotation(Inject.class);
                // 根据注解name属性的值,从容器获取bean实例(递归调用)
                Object property = factory.getBean(annotation.name());
                // 给当前的field赋值(注入)
                injectField(field, target, property);
            }
        }
    }

    /**
     * 给field赋值
     * @param field
     * @param target
     * @param property
     */
    private void injectField(Field field, Object target, Object property) {
        try {
            if(!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(target, property);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Inject property fail.", e);
        }
    }
}
