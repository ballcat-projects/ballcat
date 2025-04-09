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
	 * 根据 Excel 的文件名推断响应的 media type。
	 * @param filename excel 文件名
	 */
	public static String resolveMediaType(String filename) {
		return MediaTypeFactory.getMediaType(filename).map(MediaType::toString).orElse("application/vnd.ms-excel");
	}

	/**
	 * 设置响应头.
	 * @param response 响应
	 * @param filename 文件名
	 */
	public static void setResponseHeader(HttpServletResponse response, String filename) {
		String contentType = resolveMediaType(filename);
		setResponseHeader(response, filename, contentType);
	}

	/**
	 * 设置响应头.
	 * @param response 响应
	 * @param filename 文件名
	 * @param contentType 内容类型
	 */
	public static void setResponseHeader(HttpServletResponse response, String filename, String contentType) {
		response.setContentType(contentType);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + filename);
	}

}
