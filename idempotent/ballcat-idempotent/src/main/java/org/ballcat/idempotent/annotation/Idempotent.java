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

package org.ballcat.idempotent.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 幂等控制注解
 *
 * @author hccake
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

	String KEY_PREFIX = "idem";

	/**
	 * <p>
	 * 幂等标识的前缀，可用于区分服务和业务，防止 key 冲突
	 * </p>
	 * ps: 完整的幂等标识 = {prefix}:{uniqueExpression.value}
	 * @return 业务标识
	 */
	String prefix() default KEY_PREFIX;

	/**
	 * 值为 SpEL 表达式，从上下文中提取幂等的唯一性标识。
	 * @return Spring-EL expression
	 */
	String uniqueExpression() default "";

	/**
	 * <p>
	 * 幂等的控制时长，必须大于业务的处理耗时
	 * </p>
	 * 其值为幂等 key 的标记时长，超过标记时间，则幂等 key 可再次使用。
	 * @return 标记时长，默认 10 min
	 */
	long duration() default 10 * 60;

	/**
	 * 控制时长单位，默认为 SECONDS 秒
	 * @return {@link TimeUnit}
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

	/**
	 * 提示信息，正在执行中的提示
	 * @return 提示信息
	 */
	String message() default "重复请求，请稍后重试";

	/**
	 * 是否在业务完成后立刻清除，幂等 key
	 * @return boolean true: 立刻清除 false: 不处理
	 */
	boolean removeKeyWhenFinished() default false;

	/**
	 * 是否在业务执行异常时立刻清除幂等 key
	 * @return boolean true: 立刻清除 false: 不处理
	 */
	boolean removeKeyWhenError() default false;

}
