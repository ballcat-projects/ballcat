package com.hccake.starter.pay.wx;

import com.hccake.starte.pay.wx.WxPay;
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
@ConditionalOnClass(WxPay.class)
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(WxPay.class)
	public WxPay wxPay(WxPayProperties properties) {
		// 获取配置
		WxPayProperties.Config config = properties.getSandbox() ? properties.getDev() : properties.getProd();

		WxPay wxPay = new WxPay(config.getAppId(), config.getMchId(), config.getMckKey(), properties.getSandbox());

		wxPay.setReturnUrl(config.getReturnUrl());
		wxPay.setNotifyUrl(config.getNotifyUrl());
		return wxPay;
	}

}
