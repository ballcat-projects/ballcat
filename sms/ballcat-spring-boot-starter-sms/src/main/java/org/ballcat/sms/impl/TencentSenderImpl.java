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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ballcat.sms.SmsSender;
import org.ballcat.sms.SmsSenderParams;
import org.ballcat.sms.SmsSenderResult;
import org.ballcat.sms.enums.TypeEnum;
import org.ballcat.sms.properties.SmsProperties;
import org.ballcat.sms.properties.extra.Tencent;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lingting 2020/4/26 10:03
 */
@ConditionalOnProperty(name = "ballcat.sms.type", havingValue = "TENCENT")
public class TencentSenderImpl extends BaseServiceImpl implements SmsSender<SmsSenderParams, SmsSenderResult> {

	private final Credential cred;

	private final Tencent tencent;

	private final ObjectMapper om;

	public TencentSenderImpl(SmsProperties properties, ObjectMapper om) {
		tencent = properties.getTencent();
		cred = new Credential(properties.getId(), properties.getKey());
		this.om = om;
	}

	@Override
	public SmsSenderResult send(SmsSenderParams sp) {
		try {
			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setEndpoint(tencent.getEndpoint());

			ClientProfile clientProfile = new ClientProfile();
			clientProfile.setHttpProfile(httpProfile);

			SmsClient client = new SmsClient(cred, tencent.getRegion(), clientProfile);

			Map<String, Object> json = new HashMap<>(5);
			json.put("PhoneNumberSet", sp.getPhoneNumbers());

			json.put("SmsSdkAppid", tencent.getSdkId());

			if (tencent.getTemplateId() != null) {
				json.put("TemplateID", tencent.getTemplateId());
			}

			if (StringUtils.hasText(tencent.getSign())) {
				json.put("Sign", tencent.getSign());
			}
			if (!sp.getTemplateParam().isEmpty()) {
				json.put("TemplateParamSet", sp.getTemplateParam());
			}
			SendSmsRequest req = SendSmsRequest.fromJsonString(om.writeValueAsString(json), SendSmsRequest.class);
			SendSmsResponse resp = client.SendSms(req);
			return SmsSenderResult.generate(SendSmsRequest.toJsonString(resp), sp.toString(), sp.getPhoneNumbers());
		}
		catch (TencentCloudSDKException | JsonProcessingException e) {
			return errRet(TypeEnum.TENCENT, sp.getPhoneNumbers(), "腾讯云平台发送短信出现异常!", e);
		}
	}

}