package org.ballcat.sms;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.ballcat.sms.aliyun.AliyunSender;
import org.ballcat.sms.properties.SmsProperties;
import org.ballcat.sms.sender.AbstractSmsSender;
import org.ballcat.sms.tencent.TencentSender;
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
	@ConditionalOnMissingBean(AbstractSmsSender.class)
	@ConditionalOnProperty(name = "ballcat.sms.type", havingValue = "TENCENT")
	public TencentSender tencentSmsSender() {
		return new TencentSender(properties);
	}

	@Bean
	@SneakyThrows(Exception.class)
	@ConditionalOnMissingBean(AbstractSmsSender.class)
	@ConditionalOnProperty(name = "ballcat.sms.type", havingValue = "ALIYUN")
	public AliyunSender aliyunSmsSender() {
		return new AliyunSender(properties);
	}

}
