package com.hccake.ballcat.common.desensitize.json.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hccake.ballcat.common.desensitize.json.JsonDesensitizeSerializer;
import com.hccake.ballcat.common.desensitize.enums.SlideDesensitizationTypeEnum;
import com.hccake.ballcat.common.desensitize.handler.SlideDesensitizationHandler;

import java.lang.annotation.*;

/**
 * Jackson Filed 序列化脱敏注解, 对应使用滑动脱敏处理器对值进行脱敏处理
 * @see SlideDesensitizationHandler
 * @author Hccake 2021/1/22
 * @version 1.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = JsonDesensitizeSerializer.class)
public @interface JsonSlideDesensitize {

	/**
	 * 脱敏类型，只有当值为 CUSTOM 时，以下三个参数才有效
	 * @see SlideDesensitizationTypeEnum#CUSTOM
	 * @return type
	 */
	SlideDesensitizationTypeEnum type();

	/**
	 * 左边的明文数，只有当type值为 CUSTOM 时，才生效
	 */
	int leftPlainTextLen() default 0;

	/**
	 * 右边的明文数，只有当type值为 CUSTOM 时，才生效
	 */
	int rightPlainTextLen() default 0;

	/**
	 * 剩余部分字符逐个替换的字符串，只有当type值为 CUSTOM 时，才生效
	 */
	String maskString() default "*";

}
