package com.hccake.ballcat.common.desensitize.json.annotation;

import com.hccake.ballcat.common.desensitize.handler.SimpleDesensitizationHandler;

import java.lang.annotation.*;

/**
 * Jackson Filed 序列化脱敏注解 使用脱敏处理器对值进行脱敏处理
 * @see SimpleDesensitizationHandler
 * @author Hccake 2021/1/22
 * @version 1.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonSimpleDesensitize {

	/**
	 * 脱敏类型，用于指定脱敏处理器
	 * @return type
	 */
	Class<? extends SimpleDesensitizationHandler> handler();

}
