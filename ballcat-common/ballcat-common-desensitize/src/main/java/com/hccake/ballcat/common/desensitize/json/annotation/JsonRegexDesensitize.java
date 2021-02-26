package com.hccake.ballcat.common.desensitize.json.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hccake.ballcat.common.desensitize.json.JsonDesensitizeSerializer;
import com.hccake.ballcat.common.desensitize.enums.RegexDesensitizationTypeEnum;
import com.hccake.ballcat.common.desensitize.handler.RegexDesensitizationHandler;

import java.lang.annotation.*;

/**
 * Jackson Filed 序列化脱敏注解, 对应使用正则脱敏处理器对值进行脱敏处理
 * @see RegexDesensitizationHandler
 * @author Hccake 2021/1/22
 * @version 1.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = JsonDesensitizeSerializer.class)
public @interface JsonRegexDesensitize {

	/**
	 * 脱敏类型，用于指定正则处理方式。 只有当值为 CUSTOM 时，以下两个个参数才有效
	 * @see RegexDesensitizationTypeEnum#CUSTOM
	 * @return type
	 */
	RegexDesensitizationTypeEnum type();

	/**
	 * 匹配的正则表达式，只有当type值为 CUSTOM 时，才生效
	 */
	String regex() default "^[\\s\\S]*$";

	/**
	 * 替换规则，只有当type值为 CUSTOM 时，才生效
	 */
	String replacement() default "******";

}
