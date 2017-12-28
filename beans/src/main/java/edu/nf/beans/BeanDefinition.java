package edu.nf.beans;

public class BeanDefinition {

    /**
     * bean的作用域(创建方式)
     */
    private String scope;

    /**
     * bean的Class
     */
    private Class<?> beanClass;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
}
