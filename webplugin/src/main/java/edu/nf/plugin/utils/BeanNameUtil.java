package edu.nf.plugin.utils;

import edu.nf.beans.annotations.Component;

public class BeanNameUtil {

	private final static String PATH_SPLIT = "\\.";

	/**
	 * 获取@Component注解中的Bean名称
	 * @param clazz
	 * @return
	 * @throws ActionException
	 */
	public static String getBeanName(Class<?> clazz) {
		String beanName = clazz.getAnnotation(Component.class).value();
		return ("".equals(beanName)) ? toLowerBeanName((clazz.getSimpleName())) : beanName;
	}

	private static String toLowerBeanName(String beanName) {
		String[] cname = beanName.split(PATH_SPLIT);
		beanName = cname[cname.length - 1];
		beanName = beanName.substring(0, 1).toLowerCase()
				+ beanName.substring(1);
		return beanName;
	}
}
