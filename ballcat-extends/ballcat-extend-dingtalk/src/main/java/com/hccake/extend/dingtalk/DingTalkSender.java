package com.hccake.extend.dingtalk;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpRequest;
import com.hccake.extend.dingtalk.message.DingTalkMessage;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 订单消息发送
 *
 * @author lingting 2020/6/10 21:25
 */
@Getter
@Accessors(chain = true)
public class DingTalkSender {

	/**
	 * 请求路径
	 */
	private final String url;

	/**
	 * 密钥
	 */
	private String secret;

	private final Mac mac;

	@SneakyThrows(NoSuchAlgorithmException.class)
	public DingTalkSender(String url) {
		this.url = url;
		mac = Mac.getInstance("HmacSHA256");
	}

	/**
	 * 发送消息 根据参数值判断使用哪种发送方式
	 *
	 */
	public DingTalkResponse sendMessage(DingTalkMessage message) {
		if (CharSequenceUtil.isEmpty(secret)) {
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
	@SneakyThrows(InvalidKeyException.class)
	public DingTalkSender setSecret(String secret) {
		if (StringUtils.hasText(secret)) {
			this.secret = secret;
			mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
		}
		return this;
	}

	/**
	 * 获取签名后的请求路径
	 * @param timestamp 当前时间戳
	 */
	@SneakyThrows(UnsupportedEncodingException.class)
	public String secret(long timestamp) {
		return url + "&timestamp=" + timestamp + "&sign=" + URLEncoder.encode(
				Base64.encode(mac.doFinal((timestamp + "\n" + secret).getBytes(StandardCharsets.UTF_8))), "UTF-8");
	}

	/**
	 * 发起消息请求
	 * @param message 消息内容
	 * @param isSecret 是否签名 true 签名
	 * @return java.lang.String
	 */
	public DingTalkResponse request(DingTalkMessage message, boolean isSecret) {
		if (isSecret) {
			return DingTalkResponse.of(HttpRequest.post(url)
					// 设置新的请求路径
					.setUrl(secret(System.currentTimeMillis()))
					// 请求体
					.body(message.generate())
					// 获取返回值
					.execute().body());
		}
		else {
			return DingTalkResponse.of(HttpRequest.post(url).body(message.generate()).execute().body());
		}
	}

}
