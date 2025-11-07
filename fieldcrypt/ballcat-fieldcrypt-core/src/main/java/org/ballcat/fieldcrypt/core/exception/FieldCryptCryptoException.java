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

package org.ballcat.fieldcrypt.core.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

/**
 * 加解密过程异常包装。 failFast=true 时抛出该异常；failFast=false 时一般仅日志告警。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Getter
public class FieldCryptCryptoException extends RuntimeException {

	private final FieldCryptErrorCode code;

	private final String where;

	private final Map<String, String> details;

	// 兼容旧构造：无结构化字段
	public FieldCryptCryptoException(String message, Throwable cause) {
		super(message, cause);
		this.code = null;
		this.where = null;
		this.details = Collections.emptyMap();
	}

	// 新工厂：带 code/where
	public static FieldCryptCryptoException of(FieldCryptErrorCode code, String where) {
		return new FieldCryptCryptoException(code, where, null, null);
	}

	// 新工厂：带 code/where/cause
	public static FieldCryptCryptoException of(FieldCryptErrorCode code, String where, Throwable cause) {
		return new FieldCryptCryptoException(code, where, null, cause);
	}

	// 新工厂：带 code/where/details/cause
	public static FieldCryptCryptoException of(FieldCryptErrorCode code, String where, Map<String, String> details,
			Throwable cause) {
		return new FieldCryptCryptoException(code, where, details, cause);
	}

	private FieldCryptCryptoException(FieldCryptErrorCode code, String where, Map<String, String> details,
			Throwable cause) {
		super(defaultMessage(code, where, details), cause);
		this.code = code;
		this.where = where;
		this.details = details == null ? Collections.emptyMap()
				: Collections.unmodifiableMap(new LinkedHashMap<>(details));
	}

	private static String defaultMessage(FieldCryptErrorCode code, String where, Map<String, String> details) {
		StringBuilder sb = new StringBuilder();
		if (code != null) {
			sb.append(code.name());
		}
		if (where != null) {
			sb.append(" @").append(where);
		}
		if (details != null && !details.isEmpty()) {
			sb.append(" ").append(details);
		}
		return sb.length() == 0 ? "FieldCrypt error" : sb.toString();
	}

	// 链式补充 details（便捷）
	public FieldCryptCryptoException withDetail(String key, String value) {
		if (this.code == null) {
			return this;
		}
		Map<String, String> map = new LinkedHashMap<>(this.details);
		map.put(key, value);
		return new FieldCryptCryptoException(this.code, this.where, map, this.getCause());
	}

}
