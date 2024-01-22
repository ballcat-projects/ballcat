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

package org.ballcat.xss;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.xss.cleaner.JsoupXssCleaner;
import org.ballcat.xss.cleaner.XssCleaner;
import org.ballcat.xss.config.XssProperties;
import org.ballcat.xss.core.XssFilter;
import org.ballcat.xss.core.XssStringJsonDeserializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Hccake 2021/3/8
 *
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(XssProperties.class)
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = XssProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class XssAutoConfiguration {

	/**
	 * Xss 清理者
	 * @return XssCleaner
	 */
	@ConditionalOnMissingBean(XssCleaner.class)
	@Bean
	public XssCleaner xssCleaner() {
		return new JsoupXssCleaner();
	}

	/**
	 * 主要用于过滤 QueryString, Header 以及 form 中的参数
	 * @param xssProperties 安全配置类
	 * @return FilterRegistrationBean
	 */
	@Bean
	public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean(XssProperties xssProperties,
			XssCleaner xssCleaner) {
		log.debug("XSS 过滤已开启====");
		XssFilter xssFilter = new XssFilter(xssProperties, xssCleaner);
		FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>(xssFilter);
		registrationBean.setOrder(-1);
		return registrationBean;
	}

	/**
	 * 注册 Jackson 的序列化器，用于处理 json 类型参数的 xss 过滤
	 * @return Jackson2ObjectMapperBuilderCustomizer
	 */
	@Bean
	@ConditionalOnMissingBean(name = "xssJacksonCustomizer")
	@ConditionalOnBean(ObjectMapper.class)
	public Jackson2ObjectMapperBuilderCustomizer xssJacksonCustomizer(XssCleaner xssCleaner) {
		// 在反序列化时进行 xss 过滤，可以替换使用 XssStringJsonSerializer，在序列化时进行处理
		return builder -> builder.deserializerByType(String.class, new XssStringJsonDeserializer(xssCleaner));
	}

}
