package com.hccake.starte.pay.wx.domain;

import cn.hutool.http.HttpRequest;
import com.hccake.starte.pay.wx.enums.RequestSuffix;
import com.hccake.starte.pay.wx.utils.WxPayUtil;
import java.util.Map;
import lombok.SneakyThrows;

/**
 * 微信域名管理
 *
 * @author lingting 2021/1/26 16:05
 */
public class DefaultWxDomain implements WxDomain {

	private static final String FLAG = "/";

	/**
	 * 是否使用沙箱
	 */
	private final boolean sandbox;

	private DefaultWxDomain(boolean sandbox) {
		this.sandbox = sandbox;
	}

	public static DefaultWxDomain of(boolean sandbox) {
		return new DefaultWxDomain(sandbox);
	}

	@Override
	@SneakyThrows
	public String sendRequest(Map<String, String> params, RequestSuffix rs) {
		// 获取请求地址
		String url = getUrl(rs.getSuffix());
		HttpRequest post = HttpRequest.post(url).header("Content-Type", "text/xml").body(WxPayUtil.mapToXml(params));
		return post.execute().body();
	}

	/**
	 * 根据微信的建议, 这里后续需要加上主备切换的功能
	 * @return java.lang.String
	 * @author lingting 2021-01-29 17:50
	 */
	public String getDomain() {
		return MAIN1;
	}

	public String getUrl(String suffix) {
		if (suffix.startsWith(FLAG)) {
			suffix = suffix.substring(1);
		}

		if (sandbox) {
			return getDomain() + "sandboxnew/pay/" + suffix;
		}
		return getDomain() + "pay/" + suffix;
	}

}
