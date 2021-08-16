package com.hccake.ballcat.common.i18n;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 用于标注在需要国际化的 String 类型的属性上，用于标记其需要国际化。 必须在拥有 {@link I18nClass} 注解标记的类上
 * @author hccake
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface I18nField {

	/**
	 * <p>
	 * This is an alias for {@link #code}
	 * </p>
	 * @return String
	 */
	@AliasFor("code")
	String value() default "";

	/**
	 * 国际化的唯一标识, 当不传值时，则使用被标注的元素的值作为 code
	 * @return String
	 */
	@AliasFor("value")
	String code() default "";

}
