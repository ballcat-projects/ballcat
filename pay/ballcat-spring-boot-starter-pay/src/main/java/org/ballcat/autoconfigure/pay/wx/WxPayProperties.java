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

package org.ballcat.autoconfigure.pay.wx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2021/1/25 11:29
 */
@Data
@ConfigurationProperties(prefix = "ballcat.pay.wx")
public class WxPayProperties {

	/**
	 * 是否使用沙箱, 如果为 false 则使用 prod的配置初始化支付信息, 否则使用dev配置进行初始化
	 */
	private boolean sandbox = false;

	/**
	 * 线上环境配置
	 */
	private Config prod;

	/**
	 * 沙箱配置.
	 */
	private Config dev;

	@Data
	public static class Config {

		private String appId;

		private String mchId;

		private String mckKey;

		private String returnUrl;

		private String notifyUrl;

	}

}
