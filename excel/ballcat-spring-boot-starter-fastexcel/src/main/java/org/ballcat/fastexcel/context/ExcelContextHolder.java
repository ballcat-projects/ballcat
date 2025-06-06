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

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * ExcelContextHolder
 *
 * @author xm.z
 */
public final class ExcelContextHolder {

	/**
	 * 请求作用域中存储 Excel 上下文数据的 key
	 */
	private static final String EXCEL_CONTEXT_KEY = "__EXPORT_EXCEL_CONTEXT__";

	/**
	 * 存储 ZIP 压缩包名称的 key
	 */
	private static final String ZIP_FILE_NAME_KEY = "__EXPORT_ZIP_FILE_NAME__";

	private ExcelContextHolder() {
		// 私有构造函数防止实例化
	}

	/**
	 * 获取当前请求的 RequestAttributes
	 */
	private static RequestAttributes getRequestAttributes() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			throw new IllegalStateException("No request attributes found in current thread.");
		}
		return requestAttributes;
	}

	/**
	 * 获取上下文中保存 Excel 文件信息的 Map
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, ExcelExportInfo> getFilesMap() {
		Map<String, ExcelExportInfo> files = (Map<String, ExcelExportInfo>) getRequestAttributes()
			.getAttribute(EXCEL_CONTEXT_KEY, RequestAttributes.SCOPE_REQUEST);

		if (files == null) {
			files = new HashMap<>();
			getRequestAttributes().setAttribute(EXCEL_CONTEXT_KEY, files, RequestAttributes.SCOPE_REQUEST);
		}

		return files;
	}

	// ================== 单个/多个 Excel 文件操作 ==================

	/**
	 * 添加或更新一个 Excel 导出信息
	 */
	public static void putExcelExportInfo(String key, ExcelExportInfo info) {
		if (key != null && info != null) {
			getFilesMap().put(key, info);
		}
	}

	/**
	 * 获取某个 Excel 导出信息
	 */
	public static ExcelExportInfo getExcelExportInfo(String key) {
		return getFilesMap().get(key);
	}

	/**
	 * 获取当前上下文中所有 Excel 导出信息的副本（不可变）
	 */
	public static Map<String, ExcelExportInfo> getCopyOfExcelExportInfos() {
		return new HashMap<>(getFilesMap());
	}

	/**
	 * 判断是否存在指定 key 的 Excel 导出信息
	 */
	public static boolean containsExcelExportInfo(String key) {
		return key != null && getFilesMap().containsKey(key);
	}

	/**
	 * 清除所有 Excel 导出信息
	 */
	public static void clearExcelExportInfos() {
		getFilesMap().clear();
	}

	// ================== ZIP 压缩包名称操作 ==================

	/**
	 * 设置 ZIP 压缩包名称
	 */
	public static void setZipFileName(String zipFileName) {
		if (zipFileName != null) {
			getRequestAttributes().setAttribute(ZIP_FILE_NAME_KEY, zipFileName, RequestAttributes.SCOPE_REQUEST);
		}
	}

	/**
	 * 获取 ZIP 压缩包名称
	 */
	public static String getZipFileName() {
		Object obj = getRequestAttributes().getAttribute(ZIP_FILE_NAME_KEY, RequestAttributes.SCOPE_REQUEST);
		return obj instanceof String ? (String) obj : null;
	}

	/**
	 * 清除 ZIP 压缩包名称
	 */
	public static void clearZipFileName() {
		getRequestAttributes().removeAttribute(ZIP_FILE_NAME_KEY, RequestAttributes.SCOPE_REQUEST);
	}

	// ================== 整体清除 ==================

	/**
	 * 清除整个 Excel 上下文（Excel 导出信息 + ZIP 文件名）
	 */
	public static void clearExcelContext() {
		clearExcelExportInfos();
		clearZipFileName();
	}

}
