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

package org.ballcat.log.operation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ballcat.log.operation.enums.OperationTypes;
import org.springframework.core.annotation.AliasFor;

/**
 * @author Hccake 2019/10/15 18:09
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@OperationLogging(type = OperationTypes.DELETE)
public @interface DeleteOperationLogging {

	/**
	 * 日志信息
	 * @return 日志描述信息
	 */
	@AliasFor(annotation = OperationLogging.class)
	String msg();

	/**
	 * 是否保存方法入参
	 * @return boolean
	 */
	@AliasFor(annotation = OperationLogging.class)
	boolean recordParams() default true;

	/**
	 * 是否保存方法返回值
	 * @return boolean
	 */
	@AliasFor(annotation = OperationLogging.class)
	boolean recordResult() default true;

}
