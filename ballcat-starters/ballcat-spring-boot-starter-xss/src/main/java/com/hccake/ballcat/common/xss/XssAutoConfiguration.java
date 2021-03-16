package com.hccake.ballcat.common.xss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.common.xss.config.XssProperties;
import com.hccake.ballcat.common.xss.core.XssFilter;
import com.hccake.ballcat.common.xss.core.XssStringJsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake 2021/3/8
 * @version 1.0
 */
@Slf4j
@EnableConfigurationProperties(XssProperties.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = XssProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class XssAutoConfiguration {

	/**
	 * 主要用于过滤 QueryString, Header 以及 form 中的参数
	 * @param xssProperties 安全配置类
	 * @return FilterRegistrationBean
	 */
	@Bean
	public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean(XssProperties xssProperties) {
		log.debug("XSS 过滤已开启====");
		XssFilter xssFilter = new XssFilter(xssProperties);
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
	public Jackson2ObjectMapperBuilderCustomizer xssJacksonCustomizer() {
		// 在反序列化时进行 xss 过滤，可以替换使用 XssStringJsonSerializer，在序列化时进行处理
		return builder -> builder.deserializerByType(String.class, new XssStringJsonDeserializer());
	}

}
