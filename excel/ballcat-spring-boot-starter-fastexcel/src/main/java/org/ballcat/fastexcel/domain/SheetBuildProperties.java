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
	private int sheetNo = -1;

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
	 * 头生成器
	 */
	private Class<? extends HeadGenerator> headGenerateClass = HeadGenerator.class;

	public SheetBuildProperties(Sheet sheetAnnotation) {
		this.sheetNo = sheetAnnotation.sheetNo();
		this.sheetName = sheetAnnotation.sheetName();
		this.includes = sheetAnnotation.includes();
		this.excludes = sheetAnnotation.excludes();
		this.headGenerateClass = sheetAnnotation.headGenerateClass();
	}

	public SheetBuildProperties(int index) {
		this.sheetNo = index;
		this.sheetName = "sheet" + (this.sheetNo + 1);
	}

}
