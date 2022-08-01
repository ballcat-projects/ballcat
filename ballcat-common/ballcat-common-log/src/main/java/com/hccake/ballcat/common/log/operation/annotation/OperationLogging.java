package com.hccake.ballcat.common.log.operation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:09
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLogging {

	/**
	 * 日志信息
	 * @return 日志描述信息
	 */
	String msg() default "";

	/**
	 * 日志操作类型
	 * @return 日志操作类型枚举
	 */
	int type();

	/**
	 * 是否保存方法入参
	 * @return boolean
	 */
	boolean recordParams() default true;

	/**
	 * 是否保存方法返回值
	 * @return boolean
	 */
	boolean recordResult() default true;

}
