package com.hccake.starter.pay.ali;

import com.hccake.starte.pay.ali.AliPay;
import com.hccake.starte.pay.ali.constants.AliPayConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2021/1/25 11:30
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnClass(AliPay.class)
@EnableConfigurationProperties(AliPayProperties.class)
public class AliPayAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(AliPay.class)
	public AliPay aliPay(AliPayProperties properties) {
		// 获取配置
		AliPayProperties.Config config = properties.getSandbox() ? properties.getDev() : properties.getProd();

		AliPay aliPay = new AliPay(
				// 沙箱和线上使用不同的url
				properties.getSandbox() ? AliPayConstant.SERVER_URL_DEV : AliPayConstant.SERVER_URL_PROD,
				config.getAppId(), config.getPrivateKey(), config.getFormat(), config.getCharset(),
				config.getAlipayPublicKey(), config.getSignType());

		aliPay.setReturnUrl(config.getReturnUrl());
		aliPay.setNotifyUrl(config.getNotifyUrl());
		return aliPay;
	}

}
