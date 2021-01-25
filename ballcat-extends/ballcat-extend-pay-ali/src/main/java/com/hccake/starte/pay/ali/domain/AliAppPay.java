package com.hccake.starte.pay.ali.domain;

import com.alipay.api.response.AlipayTradeAppPayResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * app 拉起支付宝支付需要的信息
 *
 * @author lingting 2021/1/25 10:52
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliAppPay {

	private String id;

	private String require;

	/**
	 * 构建 app 拉起支付宝支付所需的参数
	 * @param appId app id
	 * @param response app pay 接口返回值
	 * @return com.hccake.starte.pay.ali.domain.AliAppPay
	 * @author lingting 2021-01-25 10:55
	 */
	public static AliAppPay of(String appId, AlipayTradeAppPayResponse response) {
		return of(appId, response.getBody());
	}

	/**
	 * 构建 app 拉起支付宝支付所需的参数
	 * @param appId app id
	 * @param body app pay 接口返回值中 body 字段内容
	 * @return com.hccake.starte.pay.ali.domain.AliAppPay
	 * @author lingting 2021-01-25 10:55
	 */
	public static AliAppPay of(String appId, String body) {
		AliAppPay pay = new AliAppPay();
		pay.setId(appId);
		pay.setRequire(body);
		return pay;
	}

}
