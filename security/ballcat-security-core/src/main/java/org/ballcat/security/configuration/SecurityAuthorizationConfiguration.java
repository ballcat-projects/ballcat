package org.ballcat.security.configuration;

import org.ballcat.security.password.DefaultSecurityPassword;
import org.ballcat.security.password.SecurityPassword;
import org.ballcat.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-04-06 15:56
 */
public class SecurityAuthorizationConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityPassword securityPassword(SecurityProperties properties) {
		return new DefaultSecurityPassword(properties.getPasswordSecretKey());
	}

}
