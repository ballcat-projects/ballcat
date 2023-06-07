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
package org.ballcat.sms.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ballcat.common.constant.Symbol;
import lombok.SneakyThrows;
import org.ballcat.sms.SmsSender;
import org.ballcat.sms.SmsSenderParams;
import org.ballcat.sms.SmsSenderResult;
import org.ballcat.sms.enums.TypeEnum;
import org.ballcat.sms.properties.SmsProperties;
import org.ballcat.sms.properties.extra.Aliyun;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author 疯狂的狮子Li 2022-04-21
 */
@ConditionalOnProperty(name = "ballcat.sms.type", havingValue = "ALIYUN")
public class AliyunSenderImpl extends BaseServiceImpl implements SmsSender<SmsSenderParams, SmsSenderResult> {

	private final Client client;

	private final Aliyun aliyun;

	private final ObjectMapper om;

	@SneakyThrows(Exception.class)
	public AliyunSenderImpl(SmsProperties properties, ObjectMapper om) {
		aliyun = properties.getAliyun();
		Config config = new Config()
			// 您的AccessKey ID
			.setAccessKeyId(aliyun.getAccessKeyId())
			// 您的AccessKey Secret
			.setAccessKeySecret(aliyun.getAccessKeySecret())
			// 访问的域名
			.setEndpoint(aliyun.getEndpoint());
		client = new Client(config);
		this.om = om;
	}

	@Override
	public SmsSenderResult send(SmsSenderParams sp) {
		try {
			SendSmsRequest req = new SendSmsRequest().setPhoneNumbers(String.join(Symbol.COMMA, sp.getPhoneNumbers()))
				.setSignName(aliyun.getSignName())
				.setTemplateCode(aliyun.getTemplateId())
				.setTemplateParam(om.writeValueAsString(sp.getAliyunTemplateParam()));

			SendSmsResponse resp = client.sendSms(req);
			return SmsSenderResult.generateAliyun(om.writeValueAsString(resp), sp.toString(), sp.getPhoneNumbers());
		}
		catch (Exception e) {
			return errRet(TypeEnum.ALIYUN, sp.getPhoneNumbers(), "阿里云平台发送短信出现异常!", e);
		}
	}

}
