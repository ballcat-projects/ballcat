package com.hccake.common.i18n.annotation;

import java.lang.annotation.*;

/**
 * i18n field annotation
 * @author Yakir
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface I18nField {

	/**
	 * 业务
	 * @return 业务码
	 */
	String businessCode();

	/**
	 * 范围值  若指定 则对此范围内的值进行国际化 不在范围的则使用默认值
	 * @return 范围值
	 */
	String[] rangeValue() default {};

	/**
	 * 默认值
	 */
	String defaultValue() default "";

}
