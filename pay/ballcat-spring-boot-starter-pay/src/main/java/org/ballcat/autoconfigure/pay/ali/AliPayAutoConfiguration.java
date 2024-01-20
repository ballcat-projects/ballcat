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

package org.ballcat.autoconfigure.pay.ali;

import org.ballcat.pay.ali.AliPay;
import org.ballcat.pay.ali.constants.AliPayConstant;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2021/1/25 11:30
 */
@AutoConfiguration
@ConditionalOnClass(AliPay.class)
@EnableConfigurationProperties(AliPayProperties.class)
public class AliPayAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(AliPay.class)
	public AliPay aliPay(AliPayProperties properties) {
		// 获取配置
		boolean sandbox = properties.getSandbox();
		AliPayProperties.Config config = sandbox ? properties.getDev() : properties.getProd();

		AliPay aliPay = new AliPay(
				// 沙箱和线上使用不同的url
				sandbox ? AliPayConstant.SERVER_URL_DEV : AliPayConstant.SERVER_URL_PROD, config.getAppId(),
				config.getPrivateKey(), config.getFormat(), config.getCharset(), config.getAlipayPublicKey(),
				config.getSignType());

		aliPay.setReturnUrl(config.getReturnUrl());
		aliPay.setNotifyUrl(config.getNotifyUrl());
		return aliPay;
	}

}
