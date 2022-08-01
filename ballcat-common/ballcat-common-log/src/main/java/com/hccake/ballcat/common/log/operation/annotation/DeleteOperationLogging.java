package com.hccake.ballcat.common.log.operation.annotation;

import com.hccake.ballcat.common.log.operation.enums.OperationTypes;
import org.springframework.core.annotation.AliasFor;

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
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@OperationLogging(type = OperationTypes.DELETE)
public @interface DeleteOperationLogging {

	/**
	 * 日志信息
	 * @return 日志描述信息
	 */
	@AliasFor(annotation = OperationLogging.class)
	String msg();

	/**
	 * 是否保存方法入参
	 * @return boolean
	 */
	@AliasFor(annotation = OperationLogging.class)
	boolean recordParams() default true;

	/**
	 * 是否保存方法返回值
	 * @return boolean
	 */
	@AliasFor(annotation = OperationLogging.class)
	boolean recordResult() default true;

}
