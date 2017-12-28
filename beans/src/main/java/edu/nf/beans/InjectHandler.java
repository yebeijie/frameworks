package edu.nf.beans;

/**
 * 
 * @author wangl 抽象注入器
 */
public interface InjectHandler {

	// 抽象注入行为,便于不同的注入实现,例如属性注入或方法注入
	void handle(Object target, Class<?> targetClass, BeanFactory factory);
}
