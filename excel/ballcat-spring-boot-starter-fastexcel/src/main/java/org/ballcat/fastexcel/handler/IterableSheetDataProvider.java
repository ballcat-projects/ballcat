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

import java.util.Iterator;
import java.util.List;

/**
 * 可迭代的Sheet数据提供者.
 *
 * @param <T> 数据类型
 * @author Hccake
 * @since 2.0.0
 */
public interface IterableSheetDataProvider<T> extends Iterator<List<T>> {

	int EXCEL2007_MAX_ROW_SIZE = 1048576;

	/**
	 * 获取数据类型
	 * @return 数据类型
	 */
	Class<T> getDataClass();

	/**
	 * 获取 sheet 名称前缀.
	 * @param sheetIndex sheet 索引
	 */
	default String getSheetName(int sheetIndex) {
		return "sheet" + sheetIndex + 1;
	}

	/**
	 * 获取单个sheet的最大行数
	 * @return 最大行数，如果返回负数表示不限制，默认值为 Excel2007 的最大行数 1048576
	 */
	default int getSheetRowLimit() {
		return EXCEL2007_MAX_ROW_SIZE;
	}

}
