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

package org.ballcat.pay.wx.domain;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import lombok.Setter;
import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.ballcat.pay.wx.enums.RequestSuffix;
import org.ballcat.pay.wx.utils.WxPayUtil;

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

	/**
	 * 默认的请求发起客户端
	 */
	private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().build();

	// TODO: (by evil0th) 2023/6/6 JDK11+后考虑采用java.net.http.HttpClient
	@Setter
	private OkHttpClient client = HTTP_CLIENT;

	private DefaultWxDomain(boolean sandbox) {
		this.sandbox = sandbox;
	}

	public static DefaultWxDomain of(boolean sandbox) {
		return new DefaultWxDomain(sandbox);
	}

	@SneakyThrows({ ParserConfigurationException.class, TransformerException.class, IOException.class })
	@Override
	public String sendRequest(Map<String, String> params, RequestSuffix rs) {
		// 获取请求地址
		final String url = getUrl(rs.getSuffix());
		RequestBody requestBody = RequestBody.create(WxPayUtil.mapToXml(params), MediaType.parse("text/xml"));
		Request request = new Request.Builder().url(url).post(requestBody).build();
		Call call = this.client.newCall(request);
		try (Response response = call.execute()) {
			ResponseBody responseBody = response.body();
			if (responseBody == null) {
				throw new RuntimeException("请求微信支付接口返回值为 null!");
			}
			return responseBody.string();
		}
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

		if (this.sandbox) {
			return getDomain() + "sandboxnew/pay/" + suffix;
		}
		return getDomain() + "pay/" + suffix;
	}

}
