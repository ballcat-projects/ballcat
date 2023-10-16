package org.ballcat.sms.tencent;

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

import java.util.HashMap;
import java.util.Map;

/**
 * @author lingting 2023-10-16 19:45
 */
public class TencentSender extends AbstractSmsSender<TencentSenderParams> {

	private final Tencent tencent;

	private final Credential cred;

	public TencentSender(SmsProperties properties) {
		tencent = properties.getTencent();
		cred = new Credential(properties.getId(), properties.getKey());
	}

	@Override
	public TypeEnum platform() {
		return TypeEnum.TENCENT;
	}

	@Override
	protected SmsSenderResult doSend(TencentSenderParams sp) throws Exception {
		HttpProfile httpProfile = new HttpProfile();
		httpProfile.setEndpoint(tencent.getEndpoint());

		ClientProfile clientProfile = new ClientProfile();
		clientProfile.setHttpProfile(httpProfile);

		SmsClient client = new SmsClient(cred, tencent.getRegion(), clientProfile);

		Map<String, Object> json = new HashMap<>(5);
		json.put("PhoneNumberSet", sp.getPhoneNumbers());
		json.put("SmsSdkAppid", tencent.getSdkId());

		fillSign(sp, json);
		fillTemplate(sp, json);

		SendSmsRequest req = AbstractModel.fromJsonString(JsonUtils.toJson(json), SendSmsRequest.class);
		SendSmsResponse resp = client.SendSms(req);
		return SmsSenderResult.generateTencent(AbstractModel.toJsonString(resp), sp.toString(), sp.getPhoneNumbers());
	}

	protected void fillSign(TencentSenderParams sp, Map<String, Object> json) {
		String sign = StringUtils.hasText(sp.getSign()) ? sp.getSign() : tencent.getSign();
		if (!StringUtils.hasText(sign)) {
			return;
		}
		json.put("Sign", sign);
	}

	protected void fillTemplate(TencentSenderParams sp, Map<String, Object> json) {
		Integer templateId = sp.getTemplateId() != null ? sp.getTemplateId() : tencent.getTemplateId();
		if (templateId == null) {
			return;
		}

		json.put("TemplateID", templateId);

		if (!CollectionUtils.isEmpty(sp.getTemplateParam())) {
			json.put("TemplateParamSet", sp.getTemplateParam());
		}

	}

}
