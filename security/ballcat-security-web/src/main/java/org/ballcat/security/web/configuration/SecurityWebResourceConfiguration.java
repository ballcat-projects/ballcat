package org.ballcat.security.web.configuration;

import okhttp3.OkHttpClient;
import org.ballcat.security.SecurityConstant;
import org.ballcat.security.authorize.SecurityAuthorize;
import org.ballcat.security.configuration.SecurityResourceConfiguration;
import org.ballcat.security.properties.SecurityProperties;
import org.ballcat.security.resources.SecurityResourceService;
import org.ballcat.security.web.aspectj.SecurityWebResourceAspectj;
import org.ballcat.security.web.filter.SecurityWebResourceFilter;
import org.ballcat.security.web.properties.SecurityWebProperties;
import org.ballcat.security.web.resource.DefaultRemoteResourceServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

/**
 * @author lingting 2023-03-29 21:22
 */
@AutoConfiguration
@ConditionalOnBean(SecurityResourceConfiguration.class)
public class SecurityWebResourceConfiguration {

	@Bean
	@ConditionalOnMissingFilterBean
	public FilterRegistrationBean<SecurityWebResourceFilter> securityWebResourceFilterRegistrationBean(
			SecurityResourceService service) {
		SecurityWebResourceFilter filter = new SecurityWebResourceFilter(service);
		FilterRegistrationBean<SecurityWebResourceFilter> bean = new FilterRegistrationBean<>(filter);
		bean.setOrder(SecurityConstant.ORDER_RESOURCE_SCOPE);
		return bean;
	}

	@Bean
	@ConditionalOnMissingFilterBean
	@ConditionalOnProperty(prefix = SecurityProperties.PREFIX + ".authorization", value = "remote",
			havingValue = "true")
	public SecurityResourceService securityResourceService(SecurityProperties properties) {
		String host = properties.getAuthorization().getRemoteHost();

		OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(Duration.ofSeconds(5))
			.readTimeout(Duration.ofSeconds(5));

		return new DefaultRemoteResourceServiceImpl(host, builder.build());
	}

	@Bean
	@ConditionalOnMissingBean
	public SecurityWebResourceAspectj securityWebResourceAspectj(SecurityWebProperties properties,
			SecurityAuthorize authorize) {
		return new SecurityWebResourceAspectj(properties, authorize);
	}

}
