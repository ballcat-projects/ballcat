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
package com.hccake.extend.pay.wx.domain;

import cn.hutool.http.HttpRequest;
import com.hccake.extend.pay.wx.enums.RequestSuffix;
import com.hccake.extend.pay.wx.utils.WxPayUtil;
import lombok.SneakyThrows;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.Map;

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

	@SneakyThrows({ ParserConfigurationException.class, TransformerException.class })
	@Override
	public String sendRequest(Map<String, String> params, RequestSuffix rs) {
		// 获取请求地址
		String url = getUrl(rs.getSuffix());
		HttpRequest post = HttpRequest.post(url).header("Content-Type", "text/xml").body(WxPayUtil.mapToXml(params));
		return post.execute().body();
	}

	/**
	 * 根据微信的建议, 这里后续需要加上主备切换的功能
	 * @return java.lang.String
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
