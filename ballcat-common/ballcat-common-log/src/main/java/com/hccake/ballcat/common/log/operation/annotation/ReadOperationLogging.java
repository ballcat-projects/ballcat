package com.hccake.ballcat.common.log.operation.annotation;

import com.hccake.ballcat.common.log.operation.enums.OperationTypeEnum;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:09
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@OperationLogging(type = OperationTypeEnum.READ)
public @interface ReadOperationLogging {

	/**
	 * 日志信息
	 * @return 日志描述信息
	 */
	@AliasFor(annotation = OperationLogging.class)
	String msg();

	/**
	 * 是否保存请求的参数
	 * @return boolean
	 */
	@AliasFor(annotation = OperationLogging.class)
	boolean isSaveRequestData() default true;

	/**
	 * 是否保存响应的参数
	 * @return boolean
	 */
	@AliasFor(annotation = OperationLogging.class)
	boolean isSaveResponseData() default true;

}
