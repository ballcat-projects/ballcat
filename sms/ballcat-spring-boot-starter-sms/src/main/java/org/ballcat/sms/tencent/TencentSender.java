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

package org.ballcat.sms.tencent;

import java.util.HashMap;
import java.util.Map;

import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.sms.SmsSenderResult;
import org.ballcat.sms.enums.TypeEnum;
import org.ballcat.sms.properties.SmsProperties;
import org.ballcat.sms.properties.extra.Tencent;
import org.ballcat.sms.sender.AbstractSmsSender;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2023-10-16 19:45
 */
public class TencentSender extends AbstractSmsSender<TencentSenderParams> {

	private final Tencent tencent;

	private final Credential cred;

	public TencentSender(SmsProperties properties) {
		this.tencent = properties.getTencent();
		this.cred = new Credential(properties.getId(), properties.getKey());
	}

	@Override
	public TypeEnum platform() {
		return TypeEnum.TENCENT;
	}

	@Override
	protected SmsSenderResult doSend(TencentSenderParams sp) throws Exception {
		HttpProfile httpProfile = new HttpProfile();
		httpProfile.setEndpoint(this.tencent.getEndpoint());

		ClientProfile clientProfile = new ClientProfile();
		clientProfile.setHttpProfile(httpProfile);

		SmsClient client = new SmsClient(this.cred, this.tencent.getRegion(), clientProfile);

		Map<String, Object> json = new HashMap<>(5);
		json.put("PhoneNumberSet", sp.getPhoneNumbers());
		json.put("SmsSdkAppid", this.tencent.getSdkId());

		fillSign(sp, json);
		fillTemplate(sp, json);

		SendSmsRequest req = AbstractModel.fromJsonString(JsonUtils.toJson(json), SendSmsRequest.class);
		SendSmsResponse resp = client.SendSms(req);
		return SmsSenderResult.generateTencent(AbstractModel.toJsonString(resp), sp.toString(), sp.getPhoneNumbers());
	}

	protected void fillSign(TencentSenderParams sp, Map<String, Object> json) {
		String sign = StringUtils.hasText(sp.getSign()) ? sp.getSign() : this.tencent.getSign();
		if (!StringUtils.hasText(sign)) {
			return;
		}
		json.put("Sign", sign);
	}

	protected void fillTemplate(TencentSenderParams sp, Map<String, Object> json) {
		Integer templateId = sp.getTemplateId() != null ? sp.getTemplateId() : this.tencent.getTemplateId();
		if (templateId == null) {
			return;
		}

		json.put("TemplateID", templateId);

		if (!CollectionUtils.isEmpty(sp.getTemplateParam())) {
			json.put("TemplateParamSet", sp.getTemplateParam());
		}

	}

}
