package edu.nf.web.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {

	public static String toJson(Object bean) {
		return new Gson().toJson(bean);
	}

	public static String toJson(Object bean, String format) {
		return new GsonBuilder().setDateFormat(format).create().toJson(bean);
	}

	public static String toJsonExpose(Object bean, String format) {
		return (format != null) ? new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation().setDateFormat(format)
				.create().toJson(bean) : new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation().create().toJson(bean);
	}

}
