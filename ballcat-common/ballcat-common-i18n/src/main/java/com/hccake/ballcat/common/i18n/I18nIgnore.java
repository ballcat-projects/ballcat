package com.hccake.ballcat.common.i18n;

import java.lang.annotation.*;

/**
 * 用于标注在需要国际化的 方法上，用于标记其需要国际化。
 *
 * @author hccake
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface I18nIgnore {

}
