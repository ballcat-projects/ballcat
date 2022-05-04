package com.hccake.ballcat.common.i18n;

import java.lang.annotation.*;

/**
 * 用于标注在需要国际化的 方法上，用于标记其需要国际化。 必须在拥有 {@link I18nClass} 注解标记的类上
 * @author hccake
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface I18nSupport {

	/**
	 * 是否支持国际化,默认支持
	 * @return
	 */
	boolean support() default true;

}
