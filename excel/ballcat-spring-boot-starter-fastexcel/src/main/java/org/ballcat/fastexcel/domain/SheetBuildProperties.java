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

package org.ballcat.fastexcel.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.ballcat.fastexcel.annotation.Sheet;
import org.ballcat.fastexcel.head.HeadGenerator;

/**
 * Sheet Build Properties
 *
 * @author chengbohua
 */
@Data
public class SheetBuildProperties {

	/**
	 * sheet 编号
	 */
	private int sheetNo;

	/**
	 * sheet name
	 */
	private String sheetName;

	/**
	 * 包含字段
	 */
	private String[] includes = new String[0];

	/**
	 * 排除字段
	 */
	private String[] excludes = new String[0];

	/**
	 * Head Class, 用于生成 Excel 头部，以便可以在返回数据为空时, 导出带有头信息的 Excel。
	 */
	Class<?> headClass = Object.class;

	/**
	 * 头生成器
	 */
	private Class<? extends HeadGenerator> headGenerateClass = HeadGenerator.class;

	public SheetBuildProperties(Sheet sheetAnnotation, int sheetIndex) {
		// 如果指定了 sheetNo，则使用指定的 sheetNo，否则使用 sheetIndex
		if (sheetAnnotation.sheetNo() >= 0) {
			this.sheetNo = sheetAnnotation.sheetNo();
		}
		else {
			this.sheetNo = sheetIndex;
		}
		// 如果没有指定 sheetName, 则默认使用前缀 “sheet” + {sheetNo}，如 “sheet1”
		if (StringUtils.isEmpty(sheetAnnotation.sheetName())) {
			this.sheetName = "sheet" + (this.sheetNo + 1);
		}
		else {
			this.sheetName = sheetAnnotation.sheetName();
		}
		this.includes = sheetAnnotation.includes();
		this.excludes = sheetAnnotation.excludes();
		this.headClass = sheetAnnotation.headClass();
		this.headGenerateClass = sheetAnnotation.headGenerateClass();
	}

	public SheetBuildProperties(int sheetIndex) {
		this("sheet", sheetIndex);
	}

	public SheetBuildProperties(String sheetNamePrefix, int sheetIndex) {
		this.sheetNo = sheetIndex;
		this.sheetName = sheetNamePrefix + (this.sheetNo + 1);
	}

}
