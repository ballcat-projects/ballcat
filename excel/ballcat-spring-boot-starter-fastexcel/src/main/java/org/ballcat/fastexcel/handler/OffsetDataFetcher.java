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
import java.util.function.Function;

/**
 * 基于偏移量的数据获取器.
 *
 * @param <T> 数据类型
 * @author Hccake
 * @since 2.0.0
 */
public class OffsetDataFetcher<T> implements DataFetcher<T> {

	private final BiFunction<Integer, Integer, List<T>> offsetFetcher;

	private final int batchSize;

	private final Function<List<T>, Integer> offsetCalculator;

	private int offset = 0;

	public OffsetDataFetcher(BiFunction<Integer, Integer, List<T>> offsetFetcher, int batchSize,
			Function<List<T>, Integer> offsetCalculator) {
		this.offsetFetcher = offsetFetcher;
		this.batchSize = batchSize;
		this.offsetCalculator = offsetCalculator;
	}

	@Override
	public List<T> get() {
		List<T> data = this.offsetFetcher.apply(this.offset, this.batchSize);
		this.offset = this.offsetCalculator.apply(data);
		return data;
	}

}
