package com.hccake.starter.sms.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpRequest;
import com.hccake.starter.sms.SmsSender;
import com.hccake.starter.sms.SmsSenderParams;
import com.hccake.starter.sms.SmsSenderResult;
import com.hccake.starter.sms.enums.TypeEnum;
import com.hccake.starter.sms.properties.SmsProperties;
import com.hccake.starter.sms.properties.extra.Account;
import com.hccake.starter.sms.properties.extra.TianYiHong;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.time.LocalDateTime;

/**
 * @author lingting 2020/4/26 10:03
 */
@ConditionalOnProperty(name = "ballcat.sms.type", havingValue = "TIAN_YI_HONG")
public class TianYiHongSenderImpl extends BaseServiceImpl implements SmsSender<SmsSenderParams, SmsSenderResult> {

	private final SmsProperties sp;

	private final MD5 md5 = new MD5();

	private final HttpRequest request;

	public TianYiHongSenderImpl(SmsProperties sp) {
		this.sp = sp;
		this.request = HttpRequest.get(sp.getUrl());
	}

	@Override
	public SmsSenderResult send(SmsSenderParams p) {
		try {
			Account account = sp.getAccounts().get(p.getCountry());
			TianYiHong ty = sp.getTianYiHong();
			String dateTime = DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss");

			String req = StrUtil.format("account={}&sign={}&datetime={}&content={}{}{}", account.getUsername(),
					account.getPassword(), md5.digestHex(account.getUsername() + account.getPassword() + dateTime),
					dateTime, p.getContent()
					// sender id
					, ty.getSenderIdCountry().contains(p.getCountry()) ? "&senderid=" + ty.getSenderId() : ""
					// numbers
					, StrUtil.join(",", p.getPhoneNumbers().toArray()));

			String res = request.setUrl(URLUtil.encode(req)).execute().body();
			return SmsSenderResult.generateTianYiHong(res, "方法参数:" + p.toString() + " ;请求: " + req,
					p.getPhoneNumbers());
		}
		catch (Exception e) {
			return errRet(TypeEnum.TIAN_YI_HONG, p.getPhoneNumbers(), "码平台发送短信出现异常!", e);
		}

	}

}
