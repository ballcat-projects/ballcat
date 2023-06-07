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

import lombok.Setter;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.ballcat.sms.SmsSender;
import org.ballcat.sms.SmsSenderParams;
import org.ballcat.sms.SmsSenderResult;
import org.ballcat.sms.enums.TypeEnum;
import org.ballcat.sms.properties.SmsProperties;
import org.ballcat.sms.properties.extra.Account;
import org.ballcat.sms.properties.extra.TianYiHong;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author lingting 2020/4/26 10:03
 */
@ConditionalOnProperty(name = "ballcat.sms.type", havingValue = "TIAN_YI_HONG")
public class TianYiHongSenderImpl extends BaseServiceImpl implements SmsSender<SmsSenderParams, SmsSenderResult> {

	private final SmsProperties sp;

	/**
	 * 默认的请求发起客户端
	 */
	private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().build();

	// TODO: (by evil0th) 2023/6/6 JDK11+后考虑采用java.net.http.HttpClient
	@Setter
	private OkHttpClient client = HTTP_CLIENT;

	public TianYiHongSenderImpl(SmsProperties sp) {
		this.sp = sp;
	}

	@Override
	public SmsSenderResult send(SmsSenderParams p) {
		try {
			Account account = sp.getAccounts().get(p.getCountry());
			TianYiHong ty = sp.getTianYiHong();
			String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

			final HttpUrl.Builder httpUrlBuilder = Objects.requireNonNull(HttpUrl.parse(sp.getUrl()))
				.newBuilder()
				.addQueryParameter("account", account.getUsername())
				.addQueryParameter("sign",
						DigestUtils.md5DigestAsHex((account.getUsername() + account.getPassword() + dateTime)
							.getBytes(StandardCharsets.UTF_8)))
				.addQueryParameter("datetime", dateTime)
				.addQueryParameter("content", p.getContent());
			// FIXME: (by evil0th) 2023/6/6
			// 原代码format参数个数对应不上，应该是无法调用成功的。天一弘已更名也提供SDK，考虑是否移除，有需要时再重构接入
			if (ty.getSenderIdCountry().contains(p.getCountry())) {
				httpUrlBuilder.addQueryParameter("senderid", ty.getSenderId());
			}
			HttpUrl url = httpUrlBuilder.build();
			String res = null;
			Request request = new Request.Builder().get().url(url).build();
			try (Response execute = client.newCall(request).execute()) {
				if (execute.isSuccessful() && null != execute.body()) {
					res = execute.body().string();
				}
			}
			return SmsSenderResult.generateTianYiHong(res, "方法参数:" + p + " ;请求: " + url, p.getPhoneNumbers());
		}
		catch (Exception e) {
			return errRet(TypeEnum.TIAN_YI_HONG, p.getPhoneNumbers(), "码平台发送短信出现异常!", e);
		}

	}

}
