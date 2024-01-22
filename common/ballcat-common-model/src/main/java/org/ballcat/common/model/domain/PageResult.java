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

package org.ballcat.common.model.domain;

import java.util.Collections;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页返回结果
 *
 * @author Hccake 2021/1/18
 */
@Data
@Schema(title = "分页返回结果")
public class PageResult<T> {

	/**
	 * 查询数据列表
	 */
	@Schema(title = "分页数据")
	protected List<T> records = Collections.emptyList();

	/**
	 * 总数
	 */
	@Schema(title = "数据总量")
	protected Long total = 0L;

	public PageResult() {
	}

	public PageResult(long total) {
		this.total = total;
	}

	public PageResult(List<T> records, long total) {
		this.records = records;
		this.total = total;
	}

}
