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

package org.ballcat.datascope.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限注解，注解在 Mapper类 或者 对应方法上 用于提供该 mapper 对应表，所需控制的实体信息
 *
 * @author Hccake 2020/9/27
 *
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

	/**
	 * 当前类或方法是否忽略数据权限
	 * @return boolean 默认返回 false
	 */
	boolean ignore() default false;

	/**
	 * 仅对指定资源类型进行数据权限控制，只在开启情况下有效，当该数组有值时，exclude不生效
	 * @see DataPermission#excludeResources
	 * @return 资源类型数组
	 */
	String[] includeResources() default {};

	/**
	 * 对指定资源类型跳过数据权限控制，只在开启情况下有效，当该includeResources有值时，exclude不生效
	 * @see DataPermission#includeResources
	 * @return 资源类型数组
	 */
	String[] excludeResources() default {};

}
