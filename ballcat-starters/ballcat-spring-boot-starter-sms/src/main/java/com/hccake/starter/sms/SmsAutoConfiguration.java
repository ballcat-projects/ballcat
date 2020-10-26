package com.hccake.starter.sms;

import com.hccake.starter.sms.impl.TencentSenderImpl;
import com.hccake.starter.sms.impl.TianYiHongSenderImpl;
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
	@ConditionalOnMissingBean(SmsSender.class)
	@ConditionalOnProperty(name = "ballcat.starter.sms.type", havingValue = "TENCENT")
	public SmsSender<SmsSenderParams, SmsSenderResult> tencentSmsSender() {
		return new TencentSenderImpl(properties);
	}

	@Bean
	@ConditionalOnMissingBean(SmsSender.class)
	@ConditionalOnProperty(name = "ballcat.starter.sms.type", havingValue = "TIAN_YI_HONG")
	public SmsSender<SmsSenderParams, SmsSenderResult> tianYiHongSmsSender() {
		return new TianYiHongSenderImpl(properties);
	}

}
