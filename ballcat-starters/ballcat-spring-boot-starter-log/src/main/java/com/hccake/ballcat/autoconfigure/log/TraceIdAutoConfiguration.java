package com.hccake.ballcat.autoconfigure.log;

import com.hccake.ballcat.common.log.mdc.TraceIdFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 17:29
 */
@AutoConfiguration
@ConditionalOnWebApplication
public class TraceIdAutoConfiguration {

	@Bean
	public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistrationBean() {
		FilterRegistrationBean<TraceIdFilter> registrationBean = new FilterRegistrationBean<>(new TraceIdFilter());
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registrationBean;
	}

}
