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

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.ballcat.i18n.I18nClass;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author Hccake
 */
@I18nClass
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(title = "返回体结构")
public class ApiResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(title = "返回状态码", defaultValue = "0")
	private int code;

	@Schema(title = "返回信息", defaultValue = "Success")
	private String message;

	@Schema(title = "数据", nullable = true, defaultValue = "null")
	private T data;

	public static <T> ApiResult<T> ok() {
		return ok(null);
	}

	public static <T> ApiResult<T> ok(T data) {
		return ok(data, SystemResultCode.SUCCESS.getMessage());
	}

	public static <T> ApiResult<T> ok(T data, String message) {
		return new ApiResult<T>().setCode(SystemResultCode.SUCCESS.getCode()).setData(data).setMessage(message);
	}

	public static <T> ApiResult<T> failed(int code, String message) {
		return new ApiResult<T>().setCode(code).setMessage(message);
	}

	public static <T> ApiResult<T> failed(ResultCode failMsg) {
		return failed(failMsg.getCode(), failMsg.getMessage());
	}

	public static <T> ApiResult<T> failed(ResultCode failMsg, String message) {
		return failed(failMsg.getCode(), message);
	}

}
