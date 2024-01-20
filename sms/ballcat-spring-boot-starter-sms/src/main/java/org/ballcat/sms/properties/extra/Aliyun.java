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

package org.ballcat.sms.properties.extra;

import lombok.Data;

/**
 * @author 疯狂的狮子Li 2022-04-21
 */
@Data
public class Aliyun {

	/**
	 * 配置节点
	 */
	private String endpoint = "dysmsapi.aliyuncs.com";

	private String accessKeyId;

	private String accessKeySecret;

	/*
	 * 短信签名名称
	 */
	private String signName;

	/**
	 * 短信模板ID
	 */
	private String templateId;

}
