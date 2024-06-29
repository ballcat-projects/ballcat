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

package org.ballcat.web.pageable;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ballcat.common.model.domain.PageParam;
import org.ballcat.common.model.domain.PageableConstants;

/**
 * 分页参数的入参数据控制枚举。
 * <p>
 * 会覆盖默认的全局配置。
 *
 * @see PageParam
 * @see DefaultPageParamArgumentResolver
 * @author Hccake
 * @since 1.5.10
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pageable {

	/**
	 * 当前页的参数名
	 */
	String pageParameterName() default PageableConstants.DEFAULT_PAGE_PARAMETER;

	/**
	 * 每页数据量的参数名
	 */
	String sizeParameterName() default PageableConstants.DEFAULT_SIZE_PARAMETER;

	/**
	 * 排序规则的参数名
	 */
	String sortParameterName() default PageableConstants.DEFAULT_SORT_PARAMETER;

	/**
	 * 分页查询的每页最大数据量
	 */
	int maxPageSize() default PageableConstants.DEFAULT_MAX_PAGE_SIZE;

}
