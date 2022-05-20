package com.hccake.starter.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.starter.sms.impl.AliyunSenderImpl;
import com.hccake.starter.sms.impl.TencentSenderImpl;
import com.hccake.starter.sms.impl.TianYiHongSenderImpl;
import com.hccake.starter.sms.properties.SmsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2020/4/26 9:45
 * @author 疯狂的狮子Li 2022-04-21
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties({ SmsProperties.class })
public class SmsAutoConfiguration {

	private final SmsProperties properties;

	@Bean
	@ConditionalOnMissingBean(SmsSender.class)
	@ConditionalOnProperty(name = "ballcat.sms.type", havingValue = "TENCENT")
	public SmsSender<SmsSenderParams, SmsSenderResult> tencentSmsSender(ObjectMapper om) {
		return new TencentSenderImpl(properties, om);
	}

	@Bean
	@ConditionalOnMissingBean(SmsSender.class)
	@ConditionalOnProperty(name = "ballcat.sms.type", havingValue = "TIAN_YI_HONG")
	public SmsSender<SmsSenderParams, SmsSenderResult> tianYiHongSmsSender() {
		return new TianYiHongSenderImpl(properties);
	}

	@Bean
	@ConditionalOnMissingBean(SmsSender.class)
	@ConditionalOnProperty(name = "ballcat.sms.type", havingValue = "ALIYUN")
	public SmsSender<SmsSenderParams, SmsSenderResult> aliyunSmsSender(ObjectMapper om) {
		return new AliyunSenderImpl(properties, om);
	}

}
