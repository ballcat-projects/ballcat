package com.hccake.starter.sms.impl;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.hccake.starter.sms.SmsSender;
import com.hccake.starter.sms.SmsSenderParams;
import com.hccake.starter.sms.SmsSenderResult;
import com.hccake.starter.sms.enums.TypeEnum;
import com.hccake.starter.sms.properties.SmsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author lingting 2020/4/26 10:03
 */
@ConditionalOnProperty(name = "sms.type", havingValue = "TENCENT")
public class TencentSenderImpl extends BaseServiceImpl implements SmsSender<SmsSenderParams, SmsSenderResult> {

	private final SmsProperties properties;

	private final Credential cred;

	public TencentSenderImpl(SmsProperties properties) {
		this.properties = properties;
		cred = new Credential(properties.getId(), properties.getKey());
	}

	@Override
	public SmsSenderResult send(SmsSenderParams smsSenderParams) {
		try {
			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setEndpoint(properties.getTencent().getEndpoint());

			ClientProfile clientProfile = new ClientProfile();
			clientProfile.setHttpProfile(httpProfile);

			SmsClient client = new SmsClient(cred, properties.getTencent().getRegion(), clientProfile);

			SendSmsRequest req = SendSmsRequest.fromJsonString(
					smsSenderParams.generateTencentParams(properties.getTencent()), SendSmsRequest.class);
			SendSmsResponse resp = client.SendSms(req);
			return SmsSenderResult.generate(SendSmsRequest.toJsonString(resp), smsSenderParams.toString(),
					smsSenderParams.getPhoneNumbers());
		}
		catch (TencentCloudSDKException e) {
			return errRet(TypeEnum.TENCENT, smsSenderParams.getPhoneNumbers(), "腾讯云平台发送短信出现异常!", e);
		}
	}

}
