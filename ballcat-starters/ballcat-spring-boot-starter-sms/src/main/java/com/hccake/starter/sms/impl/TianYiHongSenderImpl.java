package com.hccake.starter.sms.impl;

import cn.hutool.http.HttpUtil;
import com.hccake.starter.sms.SmsSender;
import com.hccake.starter.sms.SmsSenderParams;
import com.hccake.starter.sms.SmsSenderResult;
import com.hccake.starter.sms.enums.TypeEnum;
import com.hccake.starter.sms.properties.SmsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author lingting 2020/4/26 10:03
 */
@ConditionalOnProperty(name = "ballcat.starter.sms.type", havingValue = "TIAN_YI_HONG")
@RequiredArgsConstructor
public class TianYiHongSenderImpl extends BaseServiceImpl implements SmsSender<SmsSenderParams, SmsSenderResult> {

	private final SmsProperties properties;

	@Override
	public SmsSenderResult send(SmsSenderParams smsSenderParams) {
		try {
			String req = properties.getUrl() + "?" + smsSenderParams.generateTianYiHongParams(properties);
			String res = HttpUtil.get(req);
			return SmsSenderResult.generateTianYiHong(res, "方法参数:" + smsSenderParams.toString() + " ;请求: " + req,
					smsSenderParams.getPhoneNumbers());
		}
		catch (Exception e) {
			return errRet(TypeEnum.TIAN_YI_HONG, smsSenderParams.getPhoneNumbers(), "码平台发送短信出现异常!", e);
		}

	}

}
