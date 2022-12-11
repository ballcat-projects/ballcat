package com.hccake.common.core.test.desensite.custom;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomerDesensitize {

	/**
	 * 自定义属性示例，用户可以根据需求自定义注解属性
	 * @return String
	 */
	String type();

}
