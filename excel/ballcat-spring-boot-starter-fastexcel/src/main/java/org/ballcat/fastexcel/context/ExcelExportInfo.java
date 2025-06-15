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

import lombok.Getter;
import org.springframework.util.Assert;

/**
 * Excel export information
 *
 * @author xm.z
 */
@Getter
public final class ExcelExportInfo {

	/**
	 * Final generated Excel file name including extension.
	 */
	private String fileName;

	/**
	 * Optional template file path for content-based generation.
	 */
	private String template;

	/**
	 * Private constructor enforcing factory method usage.
	 * @param fileName target file name (with extension)
	 * @param template optional template path
	 * @throws IllegalArgumentException if fileName is blank
	 */
	private ExcelExportInfo(String fileName, String template) {
		Assert.hasText(fileName, "fileName must not be blank");
		this.fileName = fileName;
		this.template = template;
	}

	/**
	 * Factory method to create Excel export configuration.
	 * @param fileName target file name (should include extension)
	 * @param template optional template path, null for no template
	 * @return configured ExcelExportInfo instance
	 */
	public static ExcelExportInfo of(String fileName, String template) {
		return new ExcelExportInfo(fileName, template);
	}

	/**
	 * Fluent method to update fileName.
	 * @param fileName target file name (should include extension)
	 * @return this ExcelExportInfo instance for method chaining
	 * @throws IllegalArgumentException if fileName is blank
	 */
	public ExcelExportInfo withFileName(String fileName) {
		Assert.hasText(fileName, "fileName must not be blank");
		this.fileName = fileName;
		return this;
	}

	/**
	 * Fluent method to update template.
	 * @param template optional template path, null for no template
	 * @return this ExcelExportInfo instance for method chaining
	 */
	public ExcelExportInfo withTemplate(String template) {
		this.template = template;
		return this;
	}

}
