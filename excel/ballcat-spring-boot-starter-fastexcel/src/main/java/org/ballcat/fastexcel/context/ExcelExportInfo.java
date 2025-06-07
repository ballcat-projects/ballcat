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

package org.ballcat.fastexcel.context;

import lombok.Data;
import org.springframework.util.Assert;

/**
 * ExcelExportInfo
 *
 * @author xm.z
 */
@Data
public final class ExcelExportInfo {

	/**
	 * 最终生成的 Excel 文件名（含后缀）
	 */
	private String fileName;

	/**
	 * 模板路径（可为空，表示无模板）
	 */
	private String template;

	/**
	 * 私有构造函数，用于工厂方法创建实例
	 * @param fileName 最终生成的文件名（建议包含后缀）
	 * @param template 模板路径，可以为 null
	 */
	private ExcelExportInfo(String fileName, String template) {
		Assert.hasText(fileName, "fileName must not be blank");
		this.fileName = fileName;
		this.template = template;
	}

	/**
	 * 静态工厂方法：构建 ExcelExportInfo 实例
	 * @param fileName 最终生成的文件名（建议包含后缀）
	 * @param template 模板路径，可以为 null
	 * @return 构建好的 ExcelExportInfo 实例
	 */
	public static ExcelExportInfo of(String fileName, String template) {
		return new ExcelExportInfo(fileName, template);
	}

}
