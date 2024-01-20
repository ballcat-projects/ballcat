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

package org.ballcat.autoconfigure.web.pageable;

import lombok.Data;
import org.ballcat.common.model.domain.PageableConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hccake
 */
@Data
@ConfigurationProperties("ballcat.pageable")
public class PageableProperties {

	/**
	 * 当前页的参数名
	 */
	private String pageParameterName = PageableConstants.DEFAULT_PAGE_PARAMETER;

	/**
	 * 每页数据量的参数名
	 */
	private String sizeParameterName = PageableConstants.DEFAULT_SIZE_PARAMETER;

	/**
	 * 排序规则的参数名
	 */
	private String sortParameterName = PageableConstants.DEFAULT_SORT_PARAMETER;

	/**
	 * 分页查询的每页最大数据量
	 */
	private int maxPageSize = PageableConstants.DEFAULT_MAX_PAGE_SIZE;

}
