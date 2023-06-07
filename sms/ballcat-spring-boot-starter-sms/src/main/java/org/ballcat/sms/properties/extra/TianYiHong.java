/*
 * Copyright 2023 the original author or authors.
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

import java.util.List;
import lombok.Data;

/**
 * 天一泓 配置
 *
 * @author lingting 2020/5/6 17:16
 */
@Data
public class TianYiHong {

	/**
	 * 密码的密钥
	 */
	private String passwordKey;

	/**
	 * 安全密钥
	 */
	private String secretKey;

	private String senderId;

	/**
	 * 要添加 senderId 的国家
	 */
	private List<String> senderIdCountry;

}
