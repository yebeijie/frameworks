package edu.nf.plugin;

import edu.nf.beans.BeanFactory;
import edu.nf.beans.annotations.Component;
import edu.nf.plugin.utils.BeanNameUtil;
import edu.nf.web.ActionDefinition;
import edu.nf.web.HandlerFactory;

import java.lang.reflect.Method;

public class ContainerHandlerFactory implements HandlerFactory {

    /**
     * IOC容器
     */
    private BeanFactory beanFactory;

    public ContainerHandlerFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * 从IOC容器中获取Bean实例
     * @param definition
     * @return handler
     */
    public Object crateAction(ActionDefinition definition) {
        String beanName = BeanNameUtil.getBeanName(definition.getControllerClass());
        Object handler = beanFactory.getBean(beanName);
        if(handler == null){
            throw new RuntimeException("Create handler instance fail.");
        }
        return handler;

    }
}
