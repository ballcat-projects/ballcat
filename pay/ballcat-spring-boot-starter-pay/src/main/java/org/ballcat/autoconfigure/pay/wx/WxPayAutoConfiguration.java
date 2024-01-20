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

package org.ballcat.autoconfigure.pay.wx;

import org.ballcat.pay.wx.WxPay;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2021/1/25 11:30
 */
@AutoConfiguration
@ConditionalOnClass(WxPay.class)
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(WxPay.class)
	public WxPay wxPay(WxPayProperties properties) {
		// 获取配置
		WxPayProperties.Config config = properties.isSandbox() ? properties.getDev() : properties.getProd();

		WxPay wxPay = new WxPay(config.getAppId(), config.getMchId(), config.getMckKey(), properties.isSandbox());

		wxPay.setReturnUrl(config.getReturnUrl());
		wxPay.setNotifyUrl(config.getNotifyUrl());
		return wxPay;
	}

}
