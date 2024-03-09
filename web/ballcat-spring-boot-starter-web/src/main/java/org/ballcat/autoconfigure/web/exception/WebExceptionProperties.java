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

package org.ballcat.autoconfigure.web.exception;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Web 异常的配置
 */
@Data
@ConfigurationProperties(prefix = WebExceptionProperties.PREFIX)
public class WebExceptionProperties {

	public static final String PREFIX = "ballcat.web.exception";

	private ExceptionResolverConfig resolverConfig = new ExceptionResolverConfig();

	@Data
	public static class ExceptionResolverConfig {

		/**
		 * 是否隐藏异常的详细信息。
		 * <p>
		 * 仅针对于 GlobalHandlerExceptionResolver 中响应状态码为 500 的异常。
		 */
		private Boolean hideExceptionDetails = true;

		/**
		 * 设置隐藏后的提示信息。
		 */
		private String hiddenMessage = "系统异常，请联系管理员";

		/**
		 * 未设置隐藏异常信息时，空指针异常的提示信息。
		 */
		private String npeErrorMessage = "空指针异常!";

	}

}
