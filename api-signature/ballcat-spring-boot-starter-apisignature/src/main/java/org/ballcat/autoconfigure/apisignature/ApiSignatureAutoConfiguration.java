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

package org.ballcat.autoconfigure.apisignature;

import java.util.Collections;

import org.ballcat.apisignature.ApiKeyManager;
import org.ballcat.apisignature.ApiSignatureFilter;
import org.ballcat.apisignature.ApiSignatureProperties;
import org.ballcat.apisignature.NonceStore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * API 签名校验配置类。
 *
 * @author hccake
 * @since 2.0.0
 */
@EnableConfigurationProperties(ApiSignatureProperties.class)
@Configuration(proxyBeanMethods = false)
public class ApiSignatureAutoConfiguration {

	@Bean
	public FilterRegistrationBean<ApiSignatureFilter> apiSignatureFilterFilterRegistrationBean(
			ApiSignatureProperties apiSignatureProperties, ApiKeyManager apiKeyManager, NonceStore nonceStore) {
		FilterRegistrationBean<ApiSignatureFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new ApiSignatureFilter(apiSignatureProperties, apiKeyManager, nonceStore));
		registrationBean.setUrlPatterns(Collections.singleton("/*"));
		return registrationBean;
	}

}
