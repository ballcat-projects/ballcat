package org.ballcat.security.configuration;

import org.ballcat.security.authorize.SecurityAuthorize;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-03-29 21:09
 */
public class SecurityResourceConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityAuthorize securityAuthorize() {
		return new SecurityAuthorize();
	}

}
