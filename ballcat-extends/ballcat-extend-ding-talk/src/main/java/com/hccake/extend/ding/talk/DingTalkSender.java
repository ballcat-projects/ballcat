package com.hccake.extend.ding.talk;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.hccake.extend.ding.talk.message.DingTalkMessage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 订单消息发送
 *
 * @author lingting  2020/6/10 21:25
 */
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class DingTalkSender {
	/**
	 * 请求路径
	 */
	private final String url;
	/**
	 * 密钥
	 */
	private String secret;

	/**
	 * 发送消息
	 * 根据参数值判断使用哪种发送方式
	 *
	 * @author lingting  2020-06-11 00:05:51
	 */
	@SneakyThrows
	public DingTalkResponse sendMessage(DingTalkMessage message) {
		if (StrUtil.isEmpty(secret)) {
			return sendNormalMessage(message);
		} else {
			return sendSecretMessage(message);
		}
	}

	/**
	 * 未使用 加签 安全设置 直接发送
	 *
	 * @author lingting  2020-06-11 00:09:23
	 */
	public DingTalkResponse sendNormalMessage(DingTalkMessage message) {
		return DingTalkResponse.getInstance(HttpUtil.post(url, message.generate()));
	}

	/**
	 * 使用  加签 安全设置 发送
	 *
	 * @author lingting  2020-06-11 00:10:38
	 */
	@SneakyThrows
	public DingTalkResponse sendSecretMessage(DingTalkMessage message) {
		return DingTalkResponse.getInstance(HttpUtil.post(secret(), message.generate()));
	}

	/**
	 * 获取签名后的请求路径
	 *
	 * @author lingting  2020-06-11 00:13:55
	 */
	@SneakyThrows
	public String secret() {
		long timestamp = System.currentTimeMillis();
		String stringToSign = timestamp + "\n" + secret;
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
		byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
		String encode = URLEncoder.encode(Base64.encode(signData), "UTF-8");
		return url + "&timestamp=" + timestamp + "&sign=" + encode;
	}
}
