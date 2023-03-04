package com.hccake.extend.dingtalk;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.text.CharSequenceUtil;
import com.hccake.extend.dingtalk.message.DingTalkMessage;
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
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
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
		byte[] secretBytes = (timestamp + "\n" + secret).getBytes(StandardCharsets.UTF_8);
		String secretBase64 = Base64.encode(mac.doFinal(secretBytes));
		String sign = URLEncoder.encode(secretBase64, "UTF-8");
		return String.format("%s&timestamp=%s&sign=%s", url, timestamp, sign);
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

		Call call = client.newCall(request);

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
