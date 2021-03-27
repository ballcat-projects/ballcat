package com.hccake.common.core.test.desensite;

import com.hccake.ballcat.common.desensitize.handler.SimpleDesensitizationHandler;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomerDesensitize {

	/**
	 * 脱敏类型，用于指定脱敏处理器
	 * @return type
	 */
	Class<? extends SimpleDesensitizationHandler> handler();

}
