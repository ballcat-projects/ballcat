/*
 * Copyright 2023 the original author or authors.
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
package com.hccake.ballcat.autoconfigure.web.pageable;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.hccake.ballcat.common.model.domain.PageableConstants.*;

/**
 * @author hccake
 */
@Data
@ConfigurationProperties("ballcat.pageable")
public class PageableProperties {

	/**
	 * 当前页的参数名
	 */
	private String pageParameterName = DEFAULT_PAGE_PARAMETER;

	/**
	 * 每页数据量的参数名
	 */
	private String sizeParameterName = DEFAULT_SIZE_PARAMETER;

	/**
	 * 排序规则的参数名
	 */
	private String sortParameterName = DEFAULT_SORT_PARAMETER;

	/**
	 * 分页查询的每页最大数据量
	 */
	private int maxPageSize = DEFAULT_MAX_PAGE_SIZE;

}
