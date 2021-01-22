package com.hccake.ballcat.common.core.desensite;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * Jackson Filed 序列化脱敏注解
 * @author Hccake 2021/1/22
 * @version 1.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = JsonDesensitizeSerializer.class)
public @interface JsonDesensitize {

	/**
	 * 脱敏类型，用于指定脱敏处理器
	 * @see DesensitizationHandler#getType()
	 * @return type
	 */
	String type();

}
