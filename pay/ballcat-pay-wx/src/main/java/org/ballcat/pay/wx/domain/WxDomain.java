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
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import lombok.SneakyThrows;
import org.ballcat.pay.wx.WxPay;
import org.ballcat.pay.wx.constants.WxPayConstant;
import org.ballcat.pay.wx.enums.RequestSuffix;
import org.ballcat.pay.wx.enums.SignType;
import org.ballcat.pay.wx.response.WxPayResponse;
import org.ballcat.pay.wx.utils.WxPayUtil;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * @author lingting 2021/2/1 10:57
 */
public interface WxDomain {

	/**
	 * 主域名
	 */
	String MAIN1 = "https://api.mch.weixin.qq.com/";

	String MAIN2 = "https://api.weixin.qq.com/";

	/**
	 * 备用域名
	 */
	String BACKUP1 = "https://api2.mch.weixin.qq.com/";

	String BACKUP2 = "https://api2.weixin.qq.com/";

	/**
	 * 发起请求. 根据微信建议,实现类最好拥有主备域名自动切换的功能
	 * @param params 参数
	 * @param rs 请求后缀
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 */
	@SneakyThrows({ ParserConfigurationException.class, IOException.class, SAXException.class })
	default Map<String, String> request(Map<String, String> params, RequestSuffix rs) {
		String res = "";
		try {
			res = sendRequest(params, rs);
			return WxPayUtil.xmlToMap(res);
		}
		catch (Exception e) {
			// 用于处理返回值异常情况
			LoggerFactory.getLogger(getClass()).error("微信支付请求失败!返回值:\n {}", res);
			throw e;
		}
	}

	/**
	 * 发送请求
	 * @param params 参数
	 * @param rs 前缀
	 * @return java.lang.String
	 */
	String sendRequest(Map<String, String> params, RequestSuffix rs);

	/**
	 *
	 * 获取沙箱环境密钥
	 * @param wxPay 支付信息
	 * @return 微信支付响应信息
	 */
	default WxPayResponse sandbox(WxPay wxPay) {
		HashMap<String, String> map = new HashMap<>();
		map.put("mch_id", wxPay.getMchId());
		map.put("nonce_str", WxPayUtil.generateNonceStr());
		// 设置签名类型
		map.put(WxPayConstant.FIELD_SIGN_TYPE, SignType.MD5.getStr());
		// 签名
		map.put(WxPayConstant.FIELD_SIGN, WxPayUtil.sign(map, wxPay.getMckKey()));

		return WxPayResponse.of(request(map, RequestSuffix.GETSIGNKEY));
	}

}
