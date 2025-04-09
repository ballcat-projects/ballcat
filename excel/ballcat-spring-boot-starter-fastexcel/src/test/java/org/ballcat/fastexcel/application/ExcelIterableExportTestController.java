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

package org.ballcat.fastexcel.application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.handler.DataFetchIterableSheetDataProvider;
import org.ballcat.fastexcel.handler.OffsetDataFetcher;
import org.ballcat.fastexcel.handler.PageDataFetcher;
import org.ballcat.fastexcel.test.TestIterableSheetDataProvider;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hccake
 */
@RestController
@RequestMapping("/export/iterable")
public class ExcelIterableExportTestController {

	/**
	 * 测试迭代的总数据量为 2500。
	 */
	private final static int TOTAL_DATA_SIZE = 2500;

	/**
	 * 每次
	 */
	private final static int BATCH_SIZE = 500;

	@ResponseExcel(name = "test-iterable")
	@GetMapping
	public TestIterableSheetDataProvider iterable() {
		// 总共2500条数据，每批次500条，每个sheet最多1000条
		return new TestIterableSheetDataProvider(TOTAL_DATA_SIZE, BATCH_SIZE);
	}

	@ResponseExcel(name = "test-page")
	@GetMapping("/page")
	public DataFetchIterableSheetDataProvider<DemoData> page() {
		// 分页数据提供者
		BiFunction<Integer, Integer, List<DemoData>> pageFetcher = (currentPage, pageSize) -> {
			// 模拟从数据库查询, 总数据量为 2500
			// 计算当前页可以获取多少数据，第一页 1000，第三页 500
			int remindDataSize = TOTAL_DATA_SIZE - pageSize * (currentPage - 1);
			int size = Math.min(remindDataSize, pageSize);

			List<DemoData> list = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				DemoData data = new DemoData();
				data.setUsername("username" + i);
				data.setPassword("password" + i);
				list.add(data);
			}
			return list;
		};

		// 分页查询，每页 500 条数据
		PageDataFetcher<DemoData> demoDataPageDataFetcher = new PageDataFetcher<>(pageFetcher, BATCH_SIZE);
		DataFetchIterableSheetDataProvider<DemoData> sheetDataProvider = new DataFetchIterableSheetDataProvider<>(
				DemoData.class, demoDataPageDataFetcher);
		sheetDataProvider.setSheetRowLimit(1000);
		return sheetDataProvider;
	}

	@ResponseExcel(name = "test-offset")
	@GetMapping("/offset")
	public DataFetchIterableSheetDataProvider<DemoData> offset() {
		// 偏移量数据提供者
		BiFunction<Integer, Integer, List<DemoData>> offsetFetcher = (offset, batchSize) -> {
			// 计算当前页可以获取多少数据
			int remindDataSize = TOTAL_DATA_SIZE - offset;
			int size = Math.min(remindDataSize, batchSize);

			List<DemoData> list = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				DemoData data = new DemoData();
				data.setId(++offset);
				data.setUsername("username" + i);
				data.setPassword("password" + i);
				list.add(data);
			}
			return list;
		};

		// 用最后一个元素的 id 作为下一次查询的偏移量
		Function<List<DemoData>, Integer> offsetCalculator = list -> {
			if (CollectionUtils.isEmpty(list)) {
				return Integer.MAX_VALUE;
			}
			DemoData lasted = list.get(list.size() - 1);
			return lasted.getId();
		};

		// 根据游标查询，每次 500 条数据
		OffsetDataFetcher<DemoData> demoDataOffsetDataFetcher = new OffsetDataFetcher<>(offsetFetcher, BATCH_SIZE,
				offsetCalculator);
		DataFetchIterableSheetDataProvider<DemoData> sheetDataProvider = new DataFetchIterableSheetDataProvider<>(
				DemoData.class, demoDataOffsetDataFetcher);
		sheetDataProvider.setSheetRowLimit(1000);
		return sheetDataProvider;
	}

}
