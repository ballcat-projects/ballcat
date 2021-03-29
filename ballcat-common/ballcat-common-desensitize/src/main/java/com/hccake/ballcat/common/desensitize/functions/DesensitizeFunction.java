package com.hccake.ballcat.common.desensitize.functions;

import java.lang.annotation.Annotation;

/**
 * 脱敏函数
 *
 * @author Yakir
 */
@FunctionalInterface
public interface DesensitizeFunction {

	/**
	 * 脱敏函数
	 * @param annotation 当前脱敏注解
	 * @param value 原始值
	 * @return 脱敏处理后的值
	 */
	String desensitize(Annotation annotation, String value);

}
