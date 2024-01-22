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

package org.ballcat.dingtalk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.sun.nio.sctp.IllegalReceiveException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.ballcat.dingtalk.message.DingTalkMessage;
import org.springframework.util.StringUtils;

/**
 * 订单消息发送
 *
 * @author lingting 2020/6/10 21:25
 */
@Getter
@Accessors(chain = true)
public class DingTalkSender {

	/**
	 * 默认的请求发起客户端
	 */
	private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().build();

	/**
	 * 请求content-type
	 */
	private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

	@Setter
	private OkHttpClient client = HTTP_CLIENT;

	/**
	 * 请求路径
	 */
	private final String url;

	/**
	 * 密钥
	 */
	private String secret;

	public DingTalkSender(String url) {
		this.url = url;
	}

	/**
	 * 发送消息 根据参数值判断使用哪种发送方式
	 *
	 */
	public DingTalkResponse sendMessage(DingTalkMessage message) {
		if (!StringUtils.hasText(this.secret)) {
			return sendNormalMessage(message);
		}
		else {
			return sendSecretMessage(message);
		}
	}

	/**
	 * 未使用 加签 安全设置 直接发送
	 */
	public DingTalkResponse sendNormalMessage(DingTalkMessage message) {
		return request(message, false);
	}

	/**
	 * 使用 加签 安全设置 发送
	 */
	public DingTalkResponse sendSecretMessage(DingTalkMessage message) {
		return request(message, true);
	}

	/**
	 * 设置密钥
	 */
	public DingTalkSender setSecret(String secret) {
		this.secret = StringUtils.hasText(secret) ? secret : null;
		return this;
	}

	/**
	 * 获取签名后的请求路径
	 * @param timestamp 当前时间戳
	 */
	@SneakyThrows({ UnsupportedEncodingException.class, NoSuchAlgorithmException.class, InvalidKeyException.class })
	public String secret(long timestamp) {
		SecretKeySpec key = new SecretKeySpec(this.secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(key);

		byte[] secretBytes = (timestamp + "\n" + this.secret).getBytes(StandardCharsets.UTF_8);
		byte[] bytes = mac.doFinal(secretBytes);

		String base64 = java.util.Base64.getEncoder().encodeToString(bytes);
		String sign = URLEncoder.encode(base64, "UTF-8");
		return String.format("%s&timestamp=%s&sign=%s", this.url, timestamp, sign);
	}

	/**
	 * 发起消息请求
	 * @param dingTalkMessage 消息内容
	 * @param isSecret 是否签名 true 签名
	 * @return java.lang.String
	 */
	@SneakyThrows(IOException.class)
	public DingTalkResponse request(DingTalkMessage dingTalkMessage, boolean isSecret) {
		String message = dingTalkMessage.generate();

		String requestUrl = isSecret ? secret(System.currentTimeMillis()) : getUrl();
		RequestBody requestBody = RequestBody.create(message, MEDIA_TYPE);

		Request request = new Request.Builder().url(requestUrl).post(requestBody).build();

		Call call = this.client.newCall(request);

		try (Response response = call.execute()) {
			ResponseBody responseBody = response.body();
			if (responseBody == null) {
				throw new IllegalReceiveException("钉钉发送消息接口返回值为 null!");
			}
			String dingTalkResponse = responseBody.string();
			return DingTalkResponse.of(dingTalkResponse);
		}
	}

}
