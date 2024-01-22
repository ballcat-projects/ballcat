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

package org.ballcat.sms.properties;

import java.util.Map;

import lombok.Data;
import org.ballcat.sms.enums.TypeEnum;
import org.ballcat.sms.properties.extra.Account;
import org.ballcat.sms.properties.extra.Aliyun;
import org.ballcat.sms.properties.extra.Tencent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author lingting 2020/4/26 9:43
 * @author 疯狂的狮子Li 2022-04-21
 */
@Data
@ConfigurationProperties(prefix = "ballcat.sms")
public class SmsProperties {

	/**
	 * 类型
	 */
	private TypeEnum type = TypeEnum.CUSTOM;

	/**
	 * 请求路径
	 */
	private String url;

	/**
	 * app id
	 */
	private String id;

	/**
	 * app key
	 */
	private String key;

	/**
	 * 部分平台需要使用账号密码，都在这里配置
	 */
	private Map<String, Account> accounts;

	/**
	 * 腾讯云所需额外参数
	 */
	@NestedConfigurationProperty
	private Tencent tencent;

	/**
	 * 阿里云所需额外参数
	 */
	@NestedConfigurationProperty
	private Aliyun aliyun;

}
