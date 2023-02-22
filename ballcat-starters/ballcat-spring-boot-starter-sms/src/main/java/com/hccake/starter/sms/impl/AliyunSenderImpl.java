package com.hccake.starter.sms.impl;

import cn.hutool.core.collection.CollUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.starter.sms.SmsSender;
import com.hccake.starter.sms.SmsSenderParams;
import com.hccake.starter.sms.SmsSenderResult;
import com.hccake.starter.sms.enums.TypeEnum;
import com.hccake.starter.sms.properties.SmsProperties;
import com.hccake.starter.sms.properties.extra.Aliyun;
import lombok.SneakyThrows;
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
			SendSmsRequest req = new SendSmsRequest().setPhoneNumbers(CollUtil.join(sp.getPhoneNumbers(), ","))
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
