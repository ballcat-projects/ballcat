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

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import org.ballcat.common.constant.Symbol;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.sms.SmsSenderResult;
import org.ballcat.sms.enums.TypeEnum;
import org.ballcat.sms.properties.SmsProperties;
import org.ballcat.sms.properties.extra.Aliyun;
import org.ballcat.sms.sender.AbstractSmsSender;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2023-10-16 19:31
 */
public class AliyunSender extends AbstractSmsSender<AliYunSenderParams> {

	private final Client client;

	private final Aliyun aliyun;

	public AliyunSender(SmsProperties properties) throws Exception {
		this.aliyun = properties.getAliyun();
		Config config = new Config()
			// 您的AccessKey ID
			.setAccessKeyId(this.aliyun.getAccessKeyId())
			// 您的AccessKey Secret
			.setAccessKeySecret(this.aliyun.getAccessKeySecret())
			// 访问的域名
			.setEndpoint(this.aliyun.getEndpoint());
		this.client = new Client(config);
	}

	@Override
	public TypeEnum platform() {
		return TypeEnum.ALIYUN;
	}

	@Override
	protected SmsSenderResult doSend(AliYunSenderParams sp) throws Exception {
		String templateId = this.aliyun.getTemplateId();
		if (StringUtils.hasText(sp.getTemplateId())) {
			templateId = sp.getTemplateId();
		}

		String signName = this.aliyun.getSignName();
		if (StringUtils.hasText(sp.getSignName())) {
			signName = sp.getSignName();
		}

		SendSmsRequest req = new SendSmsRequest().setPhoneNumbers(String.join(Symbol.COMMA, sp.getPhoneNumbers()))
			.setSignName(signName)
			.setTemplateCode(templateId)
			.setTemplateParam(JsonUtils.toJson(sp.getAliyunTemplateParam()));

		SendSmsResponse resp = this.client.sendSms(req);
		return SmsSenderResult.generateAliyun(JsonUtils.toJson(resp), sp.toString(), sp.getPhoneNumbers());

	}

}
