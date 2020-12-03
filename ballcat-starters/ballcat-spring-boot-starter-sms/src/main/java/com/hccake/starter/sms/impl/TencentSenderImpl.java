package com.hccake.starter.sms.impl;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.starter.sms.SmsSender;
import com.hccake.starter.sms.SmsSenderParams;
import com.hccake.starter.sms.SmsSenderResult;
import com.hccake.starter.sms.enums.TypeEnum;
import com.hccake.starter.sms.properties.SmsProperties;
import com.hccake.starter.sms.properties.extra.Tencent;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lingting 2020/4/26 10:03
 */
@ConditionalOnProperty(name = "ballcat.sms.type", havingValue = "TENCENT")
public class TencentSenderImpl extends BaseServiceImpl implements SmsSender<SmsSenderParams, SmsSenderResult> {

	private final SmsProperties properties;

	private final Credential cred;

	private final Tencent tencent;

	private final ObjectMapper om;

	public TencentSenderImpl(SmsProperties properties, ObjectMapper om) {
		this.properties = properties;
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

			if (StrUtil.isNotEmpty(tencent.getSign())) {
				json.put("Sign", tencent.getSign());
			}
			if (sp.getTemplateParam().size() != 0) {
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
