package com.hccake.common.excel.processor;

import java.lang.reflect.Method;

/**
 * @author lengleng
 * @date 2020/3/29
 */
public interface NameProcessor {

	/**
	 * 解析名称
	 * @param args 拦截器对象
	 * @param method 当前拦截方法
	 * @param key 表达式
	 * @return String 根据表达式解析后的字符串
	 */
	String doDetermineName(Object[] args, Method method, String key);

}
