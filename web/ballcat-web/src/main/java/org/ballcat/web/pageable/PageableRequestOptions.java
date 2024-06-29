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

import lombok.Data;
import lombok.experimental.Accessors;
import org.ballcat.common.model.domain.PageableConstants;

/**
 * 分页请求参数配置项.
 *
 * @author Hccake
 * @since 2.0.0
 */
@Data
@Accessors(chain = true)
public class PageableRequestOptions {

	private String pageParameterName = PageableConstants.DEFAULT_PAGE_PARAMETER;

	private String sizeParameterName = PageableConstants.DEFAULT_SIZE_PARAMETER;

	private String sortParameterName = PageableConstants.DEFAULT_SORT_PARAMETER;

	private int maxPageSize = PageableConstants.DEFAULT_MAX_PAGE_SIZE;

}
