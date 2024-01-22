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

package org.ballcat.pay.ali;

import java.math.BigDecimal;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import lombok.Data;
import org.ballcat.pay.ali.constants.AliPayConstant;
import org.ballcat.pay.ali.domain.AliPayQuery;

/**
 * <a href="https://opendocs.alipay.com/apis">api文档</a>.
 * <a href="https://openhome.alipay.com/platform/demoManage.htm">在线调试</a>.
 *
 * @author lingting 2021/1/25 9:42
 */
@Data
public class AliPay {

	private final String serverUrl;

	private final String appId;

	private final String privateKey;

	private final String format;

	private final String charset;

	private final String alipayPublicKey;

	private String signType;

	private AlipayClient client;

	private String returnUrl;

	private String notifyUrl;

	public AliPay(String serverUrl, String appId, String privateKey, String alipayPublicKey, String signType) {
		this(serverUrl, appId, privateKey, "json", "utf-8", alipayPublicKey, signType);
	}

	public AliPay(String serverUrl, String appId, String privateKey, String format, String charset,
			String alipayPublicKey, String signType) {
		this.serverUrl = serverUrl;
		this.appId = appId;
		this.privateKey = privateKey;
		this.format = format;
		this.charset = charset;
		this.alipayPublicKey = alipayPublicKey;
		this.signType = signType;
		this.client = new DefaultAlipayClient(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType);
	}

	/**
	 * 手机网站支付-简易支付
	 * @param sn 平台订单号
	 * @param amount 用户支付金额(单位: 元)
	 * @param subject 商品标题
	 * @return com.alipay.api.response.AlipayTradeWapPayResponse
	 */
	public AlipayTradeWapPayResponse mobileWapPay(String sn, BigDecimal amount, String subject)
			throws AlipayApiException {
		return mobileWapPay(sn, amount, subject, this.returnUrl, this.notifyUrl);
	}

	/**
	 * 手机网站支付-简易支付
	 * @param sn 平台订单号
	 * @param amount 用户支付金额(单位: 元)
	 * @param subject 商品标题
	 * @return com.alipay.api.response.AlipayTradeWapPayResponse
	 */
	public AlipayTradeWapPayResponse mobileWapPay(String sn, BigDecimal amount, String subject, String returnUrl,
			String notifyUrl) throws AlipayApiException {
		AlipayTradePayModel model = new AlipayTradePayModel();
		model.setOutTradeNo(sn);
		// 单位为 元 金额需要转为 分
		model.setTotalAmount(amount.toPlainString());
		model.setSubject(subject);
		return mobileWapPay(model, returnUrl, notifyUrl);
	}

	/**
	 * 手机网站支付-复杂支付
	 */
	public AlipayTradeWapPayResponse mobileWapPay(AlipayTradePayModel model) throws AlipayApiException {
		return mobileWapPay(model, this.returnUrl, this.notifyUrl);
	}

	/**
	 * 手机网站支付-复杂支付
	 */
	public AlipayTradeWapPayResponse mobileWapPay(AlipayTradePayModel model, String returnUrl, String notifyUrl)
			throws AlipayApiException {
		AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
		request.setBizModel(model);
		request.setReturnUrl(returnUrl);
		request.setNotifyUrl(notifyUrl);
		// 网页支付方式, 将 返回值 .getBody() 内容作为 form 表单进行提交, 会跳转到一个 url, 具体参考文档
		return this.client.pageExecute(request);
	}

	/**
	 * 电脑网站支付-简易支付
	 * @param sn 平台订单号
	 * @param amount 用户支付金额(单位: 元)
	 * @param subject 商品标题
	 * @return com.alipay.api.response.AlipayTradeWapPayResponse
	 */
	public AlipayTradePagePayResponse computerWapPay(String sn, BigDecimal amount, String subject)
			throws AlipayApiException {
		return computerWapPay(sn, amount, subject, this.returnUrl, this.notifyUrl);
	}

	/**
	 * 电脑网站支付-简易支付
	 * @param sn 平台订单号
	 * @param amount 用户支付金额(单位: 元)
	 * @param subject 商品标题
	 * @return com.alipay.api.response.AlipayTradeWapPayResponse
	 */
	public AlipayTradePagePayResponse computerWapPay(String sn, BigDecimal amount, String subject, String returnUrl,
			String notifyUrl) throws AlipayApiException {
		AlipayTradePayModel model = new AlipayTradePayModel();
		model.setOutTradeNo(sn);
		// 单位为 元 金额需要转为 分
		model.setTotalAmount(amount.toPlainString());
		model.setSubject(subject);
		model.setProductCode(AliPayConstant.PRODUCT_CODE);
		return computerWapPay(model, returnUrl, notifyUrl);
	}

	/**
	 * 电脑网站支付-复杂支付
	 */
	public AlipayTradePagePayResponse computerWapPay(AlipayTradePayModel model) throws AlipayApiException {
		return computerWapPay(model, this.returnUrl, this.notifyUrl);
	}

	/**
	 * 电脑网站支付-复杂支付
	 */
	public AlipayTradePagePayResponse computerWapPay(AlipayTradePayModel model, String returnUrl, String notifyUrl)
			throws AlipayApiException {
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setReturnUrl(returnUrl);
		request.setNotifyUrl(notifyUrl);
		// 网页支付方式, 将 返回值 .getBody() 内容作为 form 表单进行提交, 会跳转到一个 url, 具体参考文档
		return this.client.pageExecute(request);
	}

	/**
	 * APP支付-简易支付
	 * @param sn 平台订单号
	 * @param amount 用户支付金额(单位: 元)
	 * @param subject 商品标题
	 * @return com.alipay.api.response.AlipayTradeWapPayResponse
	 */
	public AlipayTradeAppPayResponse appPay(String sn, BigDecimal amount, String subject) throws AlipayApiException {
		return appPay(sn, amount, subject, this.notifyUrl);
	}

	/**
	 * APP支付-简易支付
	 * @param sn 平台订单号
	 * @param amount 用户支付金额(单位: 元)
	 * @param subject 商品标题
	 * @return com.alipay.api.response.AlipayTradeAppPayResponse
	 */
	public AlipayTradeAppPayResponse appPay(String sn, BigDecimal amount, String subject, String notifyUrl)
			throws AlipayApiException {
		AlipayTradePayModel model = new AlipayTradePayModel();
		model.setOutTradeNo(sn);
		// 单位为 元 金额需要转为 分
		model.setTotalAmount(amount.toPlainString());
		model.setSubject(subject);
		return appPay(model, notifyUrl);
	}

	/**
	 * APP支付-复杂支付
	 */
	public AlipayTradeAppPayResponse appPay(AlipayTradePayModel model) throws AlipayApiException {
		return appPay(model, this.notifyUrl);
	}

	/**
	 * APP支付-复杂支付
	 */
	public AlipayTradeAppPayResponse appPay(AlipayTradePayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return this.client.sdkExecute(request);
	}

	/**
	 * 付款码支付-简易支付
	 * @param sn 平台订单号
	 * @param amount 用户支付金额(单位: 元)
	 * @param code 付款码内容(条形码或二维码内容)
	 * @param subject 商品标题
	 * @return com.alipay.api.response.AlipayTradeWapPayResponse
	 */
	public AlipayTradePayResponse codePay(String sn, BigDecimal amount, String code, String subject)
			throws AlipayApiException {
		return codePay(sn, amount, code, subject, this.notifyUrl);
	}

	/**
	 * 付款码支付-简易支付
	 * @param sn 平台订单号
	 * @param amount 用户支付金额(单位: 元)
	 * @param code 付款码内容(条形码或二维码内容)
	 * @param subject 商品标题
	 * @return com.alipay.api.response.AlipayTradeAppPayResponse
	 */
	public AlipayTradePayResponse codePay(String sn, BigDecimal amount, String code, String subject, String notifyUrl)
			throws AlipayApiException {
		AlipayTradePayModel model = new AlipayTradePayModel();
		model.setOutTradeNo(sn);
		// 单位为 元 金额需要转为 分
		model.setTotalAmount(amount.toPlainString());
		// 付款码内容
		model.setAuthCode(code);
		model.setSubject(subject);
		return codePay(model, notifyUrl);
	}

	/**
	 * 付款码支付-复杂支付
	 */
	public AlipayTradePayResponse codePay(AlipayTradePayModel model) throws AlipayApiException {
		return codePay(model, this.notifyUrl);
	}

	/**
	 * 付款码支付-复杂支付
	 */
	public AlipayTradePayResponse codePay(AlipayTradePayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		request.setBizModel(model);
		// 付款码场景固定
		model.setScene("bar_code");
		request.setNotifyUrl(notifyUrl);
		return this.client.execute(request);
	}

	/**
	 * 二维码付款-简易支付
	 * @param sn 平台订单号
	 * @param amount 用户支付金额(单位: 元)
	 * @param subject 商品标题
	 * @return com.alipay.api.response.AlipayTradePrecreateResponse
	 */
	public AlipayTradePrecreateResponse qrPay(String sn, BigDecimal amount, String subject) throws AlipayApiException {
		return qrPay(sn, amount, subject, this.notifyUrl);
	}

	/**
	 * 二维码付款-简易支付
	 * @param sn 平台订单号
	 * @param amount 用户支付金额(单位: 元)
	 * @param subject 商品标题
	 * @return com.alipay.api.response.AlipayTradePrecreateResponse
	 */
	public AlipayTradePrecreateResponse qrPay(String sn, BigDecimal amount, String subject, String notifyUrl)
			throws AlipayApiException {
		AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
		model.setOutTradeNo(sn);
		// 单位为 元 金额需要转为 分
		model.setTotalAmount(amount.toPlainString());
		model.setSubject(subject);
		return qrPay(model, notifyUrl);
	}

	/**
	 * 二维码付款-复杂支付
	 */
	public AlipayTradePrecreateResponse qrPay(AlipayTradePrecreateModel model) throws AlipayApiException {
		return qrPay(model, this.notifyUrl);
	}

	/**
	 * 二维码付款-复杂支付
	 */
	public AlipayTradePrecreateResponse qrPay(AlipayTradePrecreateModel model, String notifyUrl)
			throws AlipayApiException {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return this.client.execute(request);
	}

	/**
	 * 交易查询
	 * @param sn 平台订单号
	 * @return com.alipay.api.response.AlipayTradeQueryResponse
	 */
	public AliPayQuery query(String sn) throws AlipayApiException {
		return query(sn, null);
	}

	/**
	 * 交易查询 - 平台订单号和支付宝订单号不能同时为空
	 * @param sn 平台订单号
	 * @param tradeNo 支付宝订单号
	 * @return com.alipay.api.response.AlipayTradeQueryResponse
	 */
	public AliPayQuery query(String sn, String tradeNo) throws AlipayApiException {
		AlipayTradeQueryModel model = new AlipayTradeQueryModel();
		model.setOutTradeNo(sn);
		model.setTradeNo(tradeNo);
		return query(model);
	}

	/**
	 * 交易查询 - 复杂查询
	 * @return com.alipay.api.response.AlipayTradeQueryResponse
	 */
	public AliPayQuery query(AlipayTradeQueryModel model) throws AlipayApiException {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		return AliPayQuery.of(this.client.execute(request));
	}

	/**
	 * 交易退款
	 * @param sn 平台订单号
	 * @param amount 退款金额(单位: 元)
	 * @return com.alipay.api.response.AlipayTradeQueryResponse
	 */
	public AlipayTradeRefundResponse refund(String sn, BigDecimal amount) throws AlipayApiException {
		return refund(sn, null, amount);
	}

	/**
	 * 交易退款 - 平台订单号和支付宝订单号不能同时为空
	 * @param sn 平台订单号
	 * @param tradeNo 支付宝订单号
	 * @param amount 退款金额(单位: 元)
	 * @return com.alipay.api.response.AlipayTradeQueryResponse
	 */
	public AlipayTradeRefundResponse refund(String sn, String tradeNo, BigDecimal amount) throws AlipayApiException {
		AlipayTradeRefundModel model = new AlipayTradeRefundModel();
		model.setOutTradeNo(sn);
		model.setTradeNo(tradeNo);
		model.setRefundAmount(amount.toPlainString());
		return refund(model);
	}

	/**
	 * 交易退款 - 复杂退款
	 * @return com.alipay.api.response.AlipayTradeQueryResponse
	 */
	public AlipayTradeRefundResponse refund(AlipayTradeRefundModel model) throws AlipayApiException {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		return this.client.execute(request);
	}

	/**
	 * v1 版本验签
	 * @param map 所有参数
	 * @return boolean
	 */
	public boolean checkSignV1(Map<String, String> map) throws AlipayApiException {
		// 验签需要先移除 fund_bill_list 参数值中的 &quot; 否则会导致正确的签名验签失败
		map.put(AliPayConstant.FIELD_FUND_BILL_LIST,
				map.get(AliPayConstant.FIELD_FUND_BILL_LIST).replace("&quot;", "\""));
		return AlipaySignature.rsaCheckV1(map, this.alipayPublicKey, this.charset, this.signType);
	}

	/**
	 * v2 版本验签
	 * @param map 所有参数
	 * @return boolean
	 */
	public boolean checkSignV2(Map<String, String> map) throws AlipayApiException {
		// 验签需要先移除 fund_bill_list 参数值中的 &quot; 否则会导致正确的签名验签失败
		map.put(AliPayConstant.FIELD_FUND_BILL_LIST,
				map.get(AliPayConstant.FIELD_FUND_BILL_LIST).replace("&quot;", "\""));
		return AlipaySignature.rsaCheckV2(map, this.alipayPublicKey, this.charset, this.signType);
	}

}
