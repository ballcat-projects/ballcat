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
import java.util.function.Supplier;

/**
 * 通过数据获取的方式提供数据. 常见于数据库分批查询数据。
 *
 * @param <T> 数据类型
 * @author Hccake
 * @since 2.0.0
 */
public class DataFetchIterableSheetDataProvider<T> implements IterableSheetDataProvider<T> {

	private final Class<T> dataClass;

	private final Supplier<List<T>> dataFetcher;

	private List<T> currentBatch;

	public DataFetchIterableSheetDataProvider(Class<T> dataClass, Supplier<List<T>> dataFetcher) {
		this.dataClass = dataClass;
		this.dataFetcher = dataFetcher;
		this.currentBatch = fetchNextBatch();
	}

	@Override
	public boolean hasNext() {
		return !this.currentBatch.isEmpty();
	}

	@Override
	public List<T> next() {
		List<T> batch = this.currentBatch;
		this.currentBatch = fetchNextBatch();
		return batch;
	}

	@Override
	public Class<T> getDataClass() {
		return this.dataClass;
	}

	private List<T> fetchNextBatch() {
		return this.dataFetcher.get();
	}

}
