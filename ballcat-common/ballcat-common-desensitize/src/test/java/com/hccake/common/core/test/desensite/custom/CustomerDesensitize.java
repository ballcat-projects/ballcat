package com.hccake.common.core.test.desensite.custom;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomerDesensitize {

	/**
	 * 类型
	 * @return
	 */
	String type();

}
