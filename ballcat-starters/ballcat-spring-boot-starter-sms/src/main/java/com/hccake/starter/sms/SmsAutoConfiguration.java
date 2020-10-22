package com.hccake.starter.sms;

import com.hccake.starter.sms.impl.TencentSenderImpl;
import com.hccake.starter.sms.impl.TianYiHongSenderImpl;
import com.hccake.starter.sms.impl.XinKuKaSenderImpl;
import com.hccake.starter.sms.impl.xinkuka.service.XinKuKaSmsSendService;
import com.hccake.starter.sms.properties.SmsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2020/4/26 9:45
 */
@EnableConfigurationProperties({ SmsProperties.class })
public class SmsAutoConfiguration {

	@Autowired
	private SmsProperties properties;

	@Bean
	@ConditionalOnMissingBean(com.hccake.starter.sms.SmsSender.class)
	@ConditionalOnProperty(name = "sms.type", havingValue = "TENCENT")
	public com.hccake.starter.sms.SmsSender<com.hccake.starter.sms.SmsSenderParams, com.hccake.starter.sms.SmsSenderResult> tencentSmsSender() {
		return new TencentSenderImpl(properties);
	}

	@Bean
	@ConditionalOnMissingBean(com.hccake.starter.sms.SmsSender.class)
	@ConditionalOnProperty(name = "sms.type", havingValue = "TIAN_YI_HONG")
	public com.hccake.starter.sms.SmsSender<com.hccake.starter.sms.SmsSenderParams, com.hccake.starter.sms.SmsSenderResult> tianYiHongSmsSender() {
		return new TianYiHongSenderImpl(properties);
	}

	@Bean
	@ConditionalOnMissingBean(com.hccake.starter.sms.SmsSender.class)
	@ConditionalOnProperty(name = "sms.type", havingValue = "XIN_KU_KA")
	public com.hccake.starter.sms.SmsSender<com.hccake.starter.sms.SmsSenderParams, com.hccake.starter.sms.SmsSenderResult> xinKuKaSmsSender(
			XinKuKaSmsSendService sendService) {
		return new XinKuKaSenderImpl(properties, sendService);
	}

}
