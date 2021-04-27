package com.hccake.extend.pay.wx;

import static com.hccake.extend.pay.wx.constants.WxPayConstant.HUNDRED;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.hccake.extend.pay.wx.constants.WxPayConstant;
import com.hccake.extend.pay.wx.domain.DefaultWxDomain;
import com.hccake.extend.pay.wx.domain.WxDomain;
import com.hccake.extend.pay.wx.enums.RequestSuffix;
import com.hccake.extend.pay.wx.enums.SignType;
import com.hccake.extend.pay.wx.enums.TradeType;
import com.hccake.extend.pay.wx.response.WxPayCallback;
import com.hccake.extend.pay.wx.response.WxPayOrderQueryResponse;
import com.hccake.extend.pay.wx.response.WxPayResponse;
import com.hccake.extend.pay.wx.utils.WxPayUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author lingting 2021/1/26 15:54
 */
@Data
public class WxPay {

	private final String appId;

	private final String mchId;

	private final String mckKey;

	private String notifyUrl;

	private String returnUrl;

	/**
	 * 是否使用沙箱
	 */
	private boolean sandbox;

	/**
	 * 域名策略, 可是使用自定义实现
	 */
	private WxDomain domain;

	public WxPay(String appId, String mchId, String mckKey, boolean sandbox) {
		this(appId, mchId, mckKey, sandbox, DefaultWxDomain.of(sandbox));
	}

	public WxPay(String appId, String mchId, String mckKey, boolean sandbox, WxDomain domain) {
		this.sandbox = sandbox;
		this.domain = domain;
		this.mchId = mchId;
		this.appId = appId;

		// 沙箱环境初始化
		if (sandbox) {
			this.mckKey = domain.sandbox(this).getSandboxSignKey();
		}
		else {
			this.mckKey = mckKey;
		}

	}

	/**
	 * jsApi 支付
	 * @param sn 订单号
	 * @param amount 支付金额, 单位 元
	 * @param ip 客户端ip
	 * @param body 商品描述
	 */
	public WxPayResponse jsApiPay(String sn, BigDecimal amount, String ip, String body) {
		return jsApiPay(sn, amount, ip, body, notifyUrl);
	}

	public WxPayResponse jsApiPay(String sn, BigDecimal amount, String ip, String body, String notifyUrl) {
		return WxPayResponse.of(pay(sn, amount, ip, body, notifyUrl, TradeType.JSAPI));
	}

	/**
	 * app 支付
	 * @param sn 订单号
	 * @param amount 支付金额, 单位 元
	 * @param ip 客户端ip
	 * @param body 商品描述
	 */
	public WxPayResponse appPay(String sn, BigDecimal amount, String ip, String body) {
		return appPay(sn, amount, ip, body, notifyUrl);
	}

	public WxPayResponse appPay(String sn, BigDecimal amount, String ip, String body, String notifyUrl) {
		return WxPayResponse.of(pay(sn, amount, ip, body, notifyUrl, TradeType.APP));
	}

	/**
	 * native 支付
	 * @param sn 订单号
	 * @param amount 支付金额, 单位 元
	 * @param body 商品描述
	 */
	public WxPayResponse nativePay(String sn, BigDecimal amount, String body) {
		return nativePay(sn, amount, body, notifyUrl);
	}

	public WxPayResponse nativePay(String sn, BigDecimal amount, String body, String notifyUrl) {
		return WxPayResponse.of(pay(sn, amount, null, body, notifyUrl, TradeType.NATIVE));
	}

	/**
	 * web 支付
	 * @param sn 订单号
	 * @param amount 支付金额, 单位 元
	 * @param ip 客户端ip
	 * @param body 商品描述
	 */
	public WxPayResponse webPay(String sn, BigDecimal amount, String ip, String body) {
		return webPay(sn, amount, ip, body, notifyUrl);
	}

	public WxPayResponse webPay(String sn, BigDecimal amount, String ip, String body, String notifyUrl) {
		return WxPayResponse.of(pay(sn, amount, ip, body, notifyUrl, TradeType.MWEB));
	}

	/**
	 * 发起支付
	 * @param sn 订单号
	 * @param amount 金额, 单位 元
	 * @param ip ip
	 * @param body 描述
	 * @param notifyUrl 通知
	 * @param tradeType 支付类型
	 * @author lingting 2021-02-25 10:19
	 */
	public Map<String, String> pay(String sn, BigDecimal amount, String ip, String body, String notifyUrl,
			TradeType tradeType) {
		Map<String, String> params = new HashMap<>(6);
		params.put("body", body);
		params.put("out_trade_no", sn);
		params.put("total_fee", yuanToFen(amount));
		params.put("spbill_create_ip", ip);
		params.put("notify_url", notifyUrl);
		params.put("trade_type", tradeType.toString());

		return request(params, RequestSuffix.UNIFIEDORDER);
	}

	/**
	 * 查询订单
	 * @param sn 平台订单号
	 * @param wxSn 微信订单号
	 * @return com.hccake.extend.pay.wx.response.WxPayOrderQueryResponse
	 * @author lingting 2021-02-25 15:20
	 */
	public WxPayOrderQueryResponse query(String sn, String wxSn) {
		Assert.isFalse(StrUtil.isBlank(sn) && StrUtil.isBlank(wxSn), "参数 sn 和 wxSn 不能同时为空!");
		Map<String, String> params = new HashMap<>(6);
		params.put("out_trade_no", sn);
		params.put("transaction_id", wxSn);
		return WxPayOrderQueryResponse.of(request(params, RequestSuffix.ORDERQUERY));
	}

	/**
	 * 向微信发起请求
	 * @param params 参数
	 * @param rs 请求后缀
	 * @author lingting 2021-01-29 18:12
	 */
	@SneakyThrows
	public Map<String, String> request(Map<String, String> params, RequestSuffix rs) {
		Map<String, String> map = new HashMap<>(params.size() + 3);
		map.putAll(params);

		// 添加必须参数
		map.put("appid", appId);
		map.put("mch_id", mchId);
		map.put("nonce_str", WxPayUtil.generateNonceStr());
		// 设置签名类型; 沙箱使用 md5, 正式使用 hmac sha256
		map.put(WxPayConstant.FIELD_SIGN_TYPE, sandbox ? SignType.MD5.getStr() : SignType.HMAC_SHA256.getStr());
		// 签名
		map.put(WxPayConstant.FIELD_SIGN, WxPayUtil.sign(map, mckKey));

		return domain.request(map, rs);
	}

	/**
	 * 金额单位转换, 元 转为 分
	 * @param amount 支付金额, 单位 元
	 * @return java.lang.String
	 * @author lingting 2021-01-25 10:27
	 */
	public String yuanToFen(BigDecimal amount) {
		return amount.multiply(HUNDRED).setScale(2, RoundingMode.UP).toBigInteger().toString();
	}

	/**
	 * 验证回调签名
	 * @param callback 回调数据
	 * @return java.lang.Boolean
	 * @author lingting 2021-02-25 16:01
	 */
	public boolean checkSign(WxPayCallback callback) {
		// 原签名不存在时, 直接失败
		if (StrUtil.isBlank(callback.getSign())) {
			return false;
		}

		Map<String, String> params = new HashMap<>(callback.getRaw());

		// 存在签名类型, 直接验签
		if (params.containsKey(WxPayConstant.FIELD_SIGN_TYPE)) {
			return WxPayUtil.sign(params, mckKey).equals(callback.getSign());
		}

		// 两种签名类型都试一次
		if (WxPayUtil.sign(params, SignType.HMAC_SHA256, mckKey).equals(callback.getSign())) {
			return true;
		}

		return WxPayUtil.sign(params, SignType.MD5, mckKey).equals(callback.getSign());
	}

}
