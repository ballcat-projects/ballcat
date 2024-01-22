/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.i18n;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * 用于标注在需要国际化的 String 类型的属性上，用于标记其需要国际化。 必须在拥有 {@link I18nClass} 注解标记的类上
 *
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
	 * 使用（SpEL 表达式）获取国际化code， 1,默认未 “”，表示则使用被标注的元素的值作为 code 2, 指定国际化的唯一标识属性,被指定的属性的值作为
	 * code ,当不传值时，则使用被标注的元素的值作为 code (可选) 目前支持属性类型为: String & Number(将会格式化为String) 示例:
	 * "title" 3,为了防止重复code可添加添加一个前缀 prefix(可选) 示例: "'prefix'+ "title"
	 * @return String
	 */
	@AliasFor("value")
	String code() default "";

	/**
	 * 是否进行国际化的条件判断语句（SpEL 表达式），默认未 “”，表示永远翻译
	 * @return 返回 boolean 的 SpEL 表达式
	 */
	String condition() default "";

}
