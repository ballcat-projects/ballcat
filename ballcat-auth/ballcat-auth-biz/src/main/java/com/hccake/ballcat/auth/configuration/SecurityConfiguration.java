package com.hccake.ballcat.auth.configuration;

import com.hccake.ballcat.common.security.util.PasswordUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author hccake
 */
@Configuration
public class SecurityConfiguration {

	/**
	 * 密码解析器，只在授权服务器中进行配置
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	@ConditionalOnMissingBean
	protected PasswordEncoder passwordEncoder() {
		return PasswordUtils.createDelegatingPasswordEncoder();
	}

}
