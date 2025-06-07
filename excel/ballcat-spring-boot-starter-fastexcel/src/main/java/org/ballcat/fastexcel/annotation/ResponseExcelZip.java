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

package org.ballcat.fastexcel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for exporting multiple Excel files into a ZIP package.
 *
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * {@code @ResponseExcelZip(name = "OrderData-#{currentDateTime()}.zip", value = {
 *     {@code @ResponseExcel(name = "OrderDetails", sheets = @Sheet(name = "Orders")},
 *     {@code @ResponseExcel(name = "CustomerInfo", sheets = @Sheet(name = "Customers"))
 * })}
 * </pre>
 *
 * @author xm.z
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseExcelZip {

	/**
	 * File name with optional SpEL expression support. Provides default suffix generation
	 * methods.
	 *
	 * <p>
	 * Examples:
	 * </p>
	 * <ul>
	 * <li>{@code name = "Export-#{currentDateTime()}.zip"}</li>
	 * </ul>
	 * @return The generated ZIP file name
	 */
	String name() default "ExportData-#{currentDateTime()}.zip";

	/**
	 * Array of Excel file configurations to be included in the ZIP package. Each entry
	 * represents a separate Excel file configuration.
	 * @return Array of ResponseExcel annotations defining individual Excel files
	 */
	ResponseExcel[] value();

}
