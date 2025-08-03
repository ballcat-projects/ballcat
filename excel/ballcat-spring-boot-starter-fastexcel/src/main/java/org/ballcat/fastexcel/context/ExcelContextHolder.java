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
 * Manages Excel export context information in a request-scoped thread-local storage.
 *
 * <p>
 * This class provides methods to store and retrieve Excel export metadata and ZIP archive
 * information for the current HTTP request. Uses Spring's RequestContextHolder to
 * maintain request-specific state.
 * </p>
 *
 * @author xm.z
 */
public final class ExcelContextHolder {

	/**
	 * Request-scoped attribute key for storing Excel export context data
	 */
	private static final String EXCEL_CONTEXT_KEY = "__EXPORT_EXCEL_CONTEXT__";

	/**
	 * Request-scoped attribute key for storing ZIP archive name
	 */
	private static final String ZIP_FILE_NAME_KEY = "__EXPORT_ZIP_FILE_NAME__";

	/**
	 * Private constructor to prevent instantiation of utility class
	 */
	private ExcelContextHolder() {
		// Prevent instantiation
	}

	/**
	 * Gets the current request attributes from Spring context.
	 * @return RequestAttributes instance for the current thread
	 * @throws IllegalStateException if no request attributes are available
	 */
	private static RequestAttributes getRequestAttributes() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			throw new IllegalStateException("No request attributes found in current thread.");
		}
		return requestAttributes;
	}

	/**
	 * Gets a map of Excel export info from the request context.
	 *
	 * <p>
	 * Creates a new map if none exists for the current request. The map is stored in
	 * request scope and maintains ExcelExportInfo objects keyed by name.
	 * </p>
	 * @return Map containing Excel export information
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, ExcelExportInfo> getFilesMap() {
		Map<String, ExcelExportInfo> files = (Map<String, ExcelExportInfo>) getRequestAttributes()
			.getAttribute(EXCEL_CONTEXT_KEY, RequestAttributes.SCOPE_REQUEST);

		if (files == null) {
			files = new HashMap<>(8);
			getRequestAttributes().setAttribute(EXCEL_CONTEXT_KEY, files, RequestAttributes.SCOPE_REQUEST);
		}

		return files;
	}

	// ================== Excel File Operations ==================

	/**
	 * Stores Excel export information in the context.
	 * @param key Key to identify this Excel export
	 * @param info Excel export metadata to store
	 */
	public static void putExcelExportInfo(String key, ExcelExportInfo info) {
		if (key != null && info != null) {
			getFilesMap().put(key, info);
		}
	}

	/**
	 * Retrieves Excel export information from the context.
	 * @param key Key identifying the Excel export
	 * @return ExcelExportInfo if found, null otherwise
	 */
	public static ExcelExportInfo getExcelExportInfo(String key) {
		return getFilesMap().get(key);
	}

	/**
	 * Gets an immutable copy of all stored Excel export information.
	 * @return Map containing all Excel export information
	 */
	public static Map<String, ExcelExportInfo> getCopyOfExcelExportInfos() {
		return new HashMap<>(getFilesMap());
	}

	/**
	 * Checks if Excel export information exists for the given key.
	 * @param key Key to check
	 * @return true if information exists, false otherwise
	 */
	public static boolean containsExcelExportInfo(String key) {
		return key != null && getFilesMap().containsKey(key);
	}

	/**
	 * Clears all stored Excel export information for the current request.
	 */
	public static void clearExcelExportInfos() {
		getFilesMap().clear();
	}

	// ================== ZIP Archive Operations ==================

	/**
	 * Sets the name of the ZIP archive to be generated.
	 * @param zipFileName Name of the ZIP archive
	 */
	public static void setZipFileName(String zipFileName) {
		if (zipFileName != null) {
			getRequestAttributes().setAttribute(ZIP_FILE_NAME_KEY, zipFileName, RequestAttributes.SCOPE_REQUEST);
		}
	}

	/**
	 * Gets the ZIP archive name previously set in the context.
	 * @return ZIP archive name if set, null otherwise
	 */
	public static String getZipFileName() {
		Object obj = getRequestAttributes().getAttribute(ZIP_FILE_NAME_KEY, RequestAttributes.SCOPE_REQUEST);
		return obj instanceof String ? (String) obj : null;
	}

	/**
	 * Removes the ZIP archive name from the context.
	 */
	public static void clearZipFileName() {
		getRequestAttributes().removeAttribute(ZIP_FILE_NAME_KEY, RequestAttributes.SCOPE_REQUEST);
	}

	// ================== Context Management ==================

	/**
	 * Clears all Excel context information including both:
	 * <ul>
	 * <li>Excel export metadata</li>
	 * <li>ZIP archive name</li>
	 * </ul>
	 * Should be called at the end of request processing to prevent memory leaks.
	 */
	public static void clearExcelContext() {
		clearExcelExportInfos();
		clearZipFileName();
	}

}
