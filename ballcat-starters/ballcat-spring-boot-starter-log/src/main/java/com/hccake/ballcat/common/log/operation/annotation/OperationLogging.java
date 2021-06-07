package com.hccake.ballcat.common.log.operation.annotation;

import com.hccake.ballcat.common.log.operation.enums.OperationTypeEnum;

import java.lang.annotation.*;

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
	OperationTypeEnum type();

}
