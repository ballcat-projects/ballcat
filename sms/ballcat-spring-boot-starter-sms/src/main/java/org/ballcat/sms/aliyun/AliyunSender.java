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
		aliyun = properties.getAliyun();
		Config config = new Config()
			// 您的AccessKey ID
			.setAccessKeyId(aliyun.getAccessKeyId())
			// 您的AccessKey Secret
			.setAccessKeySecret(aliyun.getAccessKeySecret())
			// 访问的域名
			.setEndpoint(aliyun.getEndpoint());
		client = new Client(config);
	}

	@Override
	public TypeEnum platform() {
		return TypeEnum.ALIYUN;
	}

	@Override
	protected SmsSenderResult doSend(AliYunSenderParams sp) throws Exception {
		String templateId = aliyun.getTemplateId();
		if (StringUtils.hasText(sp.getTemplateId())) {
			templateId = sp.getTemplateId();
		}

		String signName = aliyun.getSignName();
		if (StringUtils.hasText(sp.getSignName())) {
			signName = sp.getSignName();
		}

		SendSmsRequest req = new SendSmsRequest().setPhoneNumbers(String.join(Symbol.COMMA, sp.getPhoneNumbers()))
			.setSignName(signName)
			.setTemplateCode(templateId)
			.setTemplateParam(JsonUtils.toJson(sp.getAliyunTemplateParam()));

		SendSmsResponse resp = client.sendSms(req);
		return SmsSenderResult.generateAliyun(JsonUtils.toJson(resp), sp.toString(), sp.getPhoneNumbers());

	}

}
