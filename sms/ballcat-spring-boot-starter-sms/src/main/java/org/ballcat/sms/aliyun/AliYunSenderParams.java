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

package org.ballcat.sms.aliyun;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.ballcat.sms.properties.SmsProperties;
import org.ballcat.sms.sender.AbstractSenderParams;

/**
 * @author lingting 2023-10-16 19:31
 */
@Getter
@Setter
public class AliYunSenderParams extends AbstractSenderParams<AliYunSenderParams> {

	/**
	 * 短信签名名称, 未指定使用 {@link SmsProperties#getAliyun()#getSignName()}
	 */
	private String signName;

	/**
	 * 短信模板ID, 如果未指定则使用 {@link SmsProperties#getAliyun()#getTemplateId()}
	 */
	private String templateId;

	/**
	 * 短信模板参数
	 */
	private Map<String, Object> aliyunTemplateParam = new HashMap<>();

}
