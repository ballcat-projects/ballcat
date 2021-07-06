package com.hccake.ballcat.common.security;

import com.hccake.ballcat.common.security.component.CustomPermissionEvaluator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hccake
 */
@Configuration(proxyBeanMethods = false)
public class CustomSecurityConfiguration {

	@Bean(name = "per")
	@ConditionalOnMissingBean(CustomPermissionEvaluator.class)
	public CustomPermissionEvaluator customPermissionEvaluator() {
		return new CustomPermissionEvaluator();
	}

}
