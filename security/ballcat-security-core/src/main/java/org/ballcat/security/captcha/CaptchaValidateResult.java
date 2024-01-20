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

package org.ballcat.security.captcha;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 验证码的校验结果
 *
 * @author hccake
 */
@Getter
@Accessors(chain = true)
public class CaptchaValidateResult {

	/**
	 * 是否成功
	 */
	private final boolean success;

	/**
	 * 信息
	 */
	private final String message;

	public CaptchaValidateResult(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public static CaptchaValidateResult success() {
		return success("success");
	}

	public static CaptchaValidateResult success(String successMessage) {
		return new CaptchaValidateResult(true, successMessage);
	}

	public static CaptchaValidateResult failure() {
		return failure("captcha validate failure");
	}

	public static CaptchaValidateResult failure(String failureMessage) {
		return new CaptchaValidateResult(false, failureMessage);
	}

}
