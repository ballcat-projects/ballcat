package com.hccake.ballcat.common.i18n;

import java.lang.annotation.*;

/**
 * 标注于需要国际化处理的类上, 配合 {@link I18nField} 使用，在响应时进行国际化处理
 * @see I18nResponseAdvice
 * @author hccake
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface I18nClass {

}
