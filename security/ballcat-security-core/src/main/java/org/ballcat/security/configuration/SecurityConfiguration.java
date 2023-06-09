package org.ballcat.security.configuration;

import org.ballcat.security.SecurityStore;
import org.ballcat.security.properties.SecurityProperties;
import org.ballcat.security.resources.SecurityScope;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lingting 2023-03-29 20:51
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityStore securityStore() {
		Map<String, SecurityScope> map = new ConcurrentHashMap<>();

		return new SecurityStore() {
			@Override
			public void save(SecurityScope scope) {
				map.put(scope.getToken(), scope);
			}

			@Override
			public void update(SecurityScope scope) {
				map.put(scope.getToken(), scope);
			}

			@Override
			public void deleted(SecurityScope scope) {
				map.remove(scope.getToken());
			}

			@Override
			public SecurityScope get(String token) {
				return map.get(token);
			}
		};
	}

}
