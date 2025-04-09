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

package org.ballcat.fastexcel.handler;

import java.util.List;
import java.util.function.BiFunction;

/**
 * 分页数据获取器.
 *
 * @param <T> 数据类型
 * @author Hccake
 * @since 2.0.0
 */
public class PageDataFetcher<T> implements DataFetcher<T> {

	private final BiFunction<Integer, Integer, List<T>> pageFetcher;

	private final int pageSize;

	private int currentPage = 1;

	public PageDataFetcher(BiFunction<Integer, Integer, List<T>> pageFetcher, int pageSize) {
		this.pageFetcher = pageFetcher;
		this.pageSize = pageSize;
	}

	@Override
	public List<T> get() {
		List<T> data = this.pageFetcher.apply(this.currentPage, this.pageSize);
		this.currentPage++;
		return data;
	}

}
