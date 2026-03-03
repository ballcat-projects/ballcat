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

package org.ballcat.desensitize.autoconfigure;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Spring Boot 配置绑定，日志脱敏配置项，基于文本脱敏规则
 *
 * @author Hccake
 */
@ConfigurationProperties(prefix = "ballcat.desensitize.logging")
@Data
public class BallcatDesensitizeLoggingProperties {

	/**
	 * 全局开关，关闭后所有日志均不脱敏
	 **/
	private boolean enabled = true;

	/**
	 * 作用域开关表（类似 logging.level 语义但非级别）： 键为 logger 名或包名，值表示是否开启脱敏；支持 root 作为默认决策。
	 */
	private Map<String, Boolean> scopes = new HashMap<>();

}
