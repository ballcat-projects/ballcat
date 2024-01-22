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
		return new TencentSender(this.properties);
	}

	@Bean
	@SneakyThrows(Exception.class)
	@ConditionalOnMissingBean(AbstractSmsSender.class)
	@ConditionalOnProperty(name = "ballcat.sms.type", havingValue = "ALIYUN")
	public AliyunSender aliyunSmsSender() {
		return new AliyunSender(this.properties);
	}

}
