package edu.nf.beans;

import edu.nf.beans.handler.FieldInjectHandler;
import edu.nf.beans.handler.MethodInjectHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 
 * 注入回调处理器
 */
public class InjectHandlerInvoker {
	
	private static List<InjectHandler> handlers = new ArrayList<>();

	/**
	 * 初始化注入处理器
	 */
	static {
		handlers.add(new FieldInjectHandler());
		handlers.add(new MethodInjectHandler());
	}

	/**
	 * 执行注入操作
	 * @param bean
	 * @param targetClass
	 * @param factory
	 * @return
	 */
	public static Object inject(Object bean, Class<?> targetClass,BeanFactory factory) {
		for(InjectHandler handler : handlers){
			handler.handle(bean, targetClass, factory);
		}
		return bean;
	}
}
