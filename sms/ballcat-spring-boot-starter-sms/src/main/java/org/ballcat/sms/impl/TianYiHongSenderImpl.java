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

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpRequest;
import org.ballcat.sms.SmsSender;
import org.ballcat.sms.SmsSenderParams;
import org.ballcat.sms.SmsSenderResult;
import org.ballcat.sms.enums.TypeEnum;
import org.ballcat.sms.properties.SmsProperties;
import org.ballcat.sms.properties.extra.Account;
import org.ballcat.sms.properties.extra.TianYiHong;
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

			String req = CharSequenceUtil.format("account={}&sign={}&datetime={}&content={}{}{}", account.getUsername(),
					account.getPassword(), md5.digestHex(account.getUsername() + account.getPassword() + dateTime),
					dateTime, p.getContent()
					// sender id
					, ty.getSenderIdCountry().contains(p.getCountry()) ? "&senderid=" + ty.getSenderId() : ""
					// numbers
					, CharSequenceUtil.join(",", p.getPhoneNumbers().toArray()));

			String res = request.setUrl(URLEncodeUtil.encode(req)).execute().body();
			return SmsSenderResult.generateTianYiHong(res, "方法参数:" + p.toString() + " ;请求: " + req,
					p.getPhoneNumbers());
		}
		catch (Exception e) {
			return errRet(TypeEnum.TIAN_YI_HONG, p.getPhoneNumbers(), "码平台发送短信出现异常!", e);
		}

	}

}
