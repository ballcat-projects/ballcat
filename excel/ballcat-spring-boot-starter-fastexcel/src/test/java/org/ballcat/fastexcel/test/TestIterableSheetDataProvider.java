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

package org.ballcat.fastexcel.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.ballcat.fastexcel.application.DemoData;
import org.ballcat.fastexcel.handler.IterableSheetDataProvider;

/**
 * 测试用的数据提供者
 */
public class TestIterableSheetDataProvider implements IterableSheetDataProvider<DemoData> {

	private final Class<DemoData> dataClass = DemoData.class;

	private final Supplier<List<DemoData>> dataFetcher;

	private List<DemoData> currentBatch;

	private int totalCount;

	private final int batchSize;

	public TestIterableSheetDataProvider(int totalCount, int batchSize) {
		this.totalCount = totalCount;
		this.batchSize = batchSize;
		this.dataFetcher = this::generateBatchData;
		this.currentBatch = fetchNextBatch();
	}

	@Override
	public boolean hasNext() {
		return !this.currentBatch.isEmpty();
	}

	@Override
	public List<DemoData> next() {
		List<DemoData> batch = this.currentBatch;
		this.currentBatch = fetchNextBatch();
		return batch;
	}

	@Override
	public Class<DemoData> getDataClass() {
		return this.dataClass;
	}

	@Override
	public int getSheetRowLimit() {
		// 设置每个sheet最多1000行
		return 1000;
	}

	private List<DemoData> fetchNextBatch() {
		return this.dataFetcher.get();
	}

	private List<DemoData> generateBatchData() {
		if (this.totalCount <= 0) {
			return new ArrayList<>();
		}

		int currentBatchSize = Math.min(this.batchSize, this.totalCount);
		List<DemoData> batch = new ArrayList<>(currentBatchSize);

		for (int i = 0; i < currentBatchSize; i++) {
			DemoData data = new DemoData();
			data.setUsername("username" + (this.totalCount - 1));
			data.setPassword("password" + (this.totalCount - 1));
			batch.add(data);
			this.totalCount--;
		}

		return batch;
	}

}
