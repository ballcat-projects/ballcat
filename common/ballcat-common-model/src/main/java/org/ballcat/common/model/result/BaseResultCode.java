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

package org.ballcat.common.model.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hccake 2019/9/12 12:19
 */
@Getter
@AllArgsConstructor
public enum BaseResultCode implements ResultCode {

	/**
	 * 数据库保存/更新异常
	 */
	UPDATE_DATABASE_ERROR(90001, "Update Database Error"),

	/**
	 * 通用的逻辑校验异常
	 */
	LOGIC_CHECK_ERROR(90004, "Logic Check Error"),

	/**
	 * 恶意请求
	 */
	MALICIOUS_REQUEST(90005, "Malicious Request"),

	/**
	 * 文件上传异常
	 */
	FILE_UPLOAD_ERROR(90006, "File Upload Error"),

	/**
	 * 重复执行
	 */
	REPEATED_EXECUTE(90007, "Repeated execute"),

	/**
	 * 未知异常
	 */
	UNKNOWN_ERROR(99999, "Unknown Error");

	private final Integer code;

	private final String message;

}
