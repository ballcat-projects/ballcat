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

package org.ballcat.fastexcel.util;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.converters.ConverterKeyBuild.ConverterKey;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

/**
 * @author lingting 2024-01-11 15:25
 * @since 2.0.0
 */
@SuppressWarnings("java:S1452")
public final class FastExcelUtils {

	private FastExcelUtils() {
	}

	public static Converter<?> find(ConverterKey key, Map<ConverterKey, Converter<?>> map) {
		// 先直接获取
		Converter<?> converter = map.get(key);
		if (converter != null) {
			return converter;
		}

		// 通过继承关系获取
		for (Map.Entry<ConverterKey, Converter<?>> entry : map.entrySet()) {
			ConverterKey converterKey = entry.getKey();
			Converter<?> value = entry.getValue();

			// 行数据类型不一致, 跳过
			if (Objects.equals(converterKey.getCellDataTypeEnum(), key.getCellDataTypeEnum())) {
				Class<?> converterClz = converterKey.getClazz();

				// Java数据类型匹配
				if (converterClz.isAssignableFrom(key.getClazz())) {
					return value;
				}
			}
		}
		return null;
	}

	/**
	 * Resolves the media type for an Excel file based on its filename.
	 * @param filename The name of the Excel file (e.g., "example.xlsx").
	 * @return The resolved media type as a string. If the media type cannot be
	 * determined, defaults to "application/vnd.ms-excel".
	 */
	public static String resolveMediaType(String filename) {
		// Determine the content type based on the actual file type.
		return MediaTypeFactory.getMediaType(filename).map(MediaType::toString).orElse("application/vnd.ms-excel");
	}

	/**
	 * Sets the HTTP response headers for downloading an Excel file. This method
	 * automatically resolves the media type based on the filename.
	 * @param response The HTTP response object.
	 * @param filename The name of the Excel file to be downloaded.
	 */
	public static void setResponseHeader(HttpServletResponse response, String filename) {
		String contentType = resolveMediaType(filename);
		setResponseHeader(response, filename, contentType);
	}

	/**
	 * Sets response headers for file download (e.g., Excel files). Configures content
	 * type, encoding, and Content-Disposition header.
	 * @param response HTTP response object
	 * @param filename File name to use for the downloaded file (including extension)
	 * @param contentType MIME type of the file
	 */
	public static void setResponseHeader(HttpServletResponse response, String filename, String contentType) {
		// Set the MIME type for the response
		response.setContentType(contentType);

		// Set character encoding to UTF-8 to prevent encoding issues in the filename
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		// Build Content-Disposition header to force browser to download the file
		// Use UTF-8 encoding when specifying the filename to support international
		// characters
		ContentDisposition contentDisposition = ContentDisposition.attachment()
				.filename(filename, StandardCharsets.UTF_8)
				.build();

		// Add the Content-Disposition header to the response
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
	}

}
