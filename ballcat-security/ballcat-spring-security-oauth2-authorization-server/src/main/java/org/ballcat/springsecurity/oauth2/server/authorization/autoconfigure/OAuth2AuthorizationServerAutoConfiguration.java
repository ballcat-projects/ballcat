package org.ballcat.springsecurity.oauth2.server.authorization.autoconfigure;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.common.security.jackson2.UserMixin;
import com.hccake.ballcat.common.security.userdetails.User;
import com.hccake.ballcat.common.security.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.oauth2.server.authorization.OAuth2AuthorizationObjectMapperCustomizer;
import org.ballcat.springsecurity.oauth2.server.authorization.config.OAuth2AuthorizationServerConfigurerAdapter;
import org.ballcat.springsecurity.oauth2.server.authorization.config.customizer.OAuth2AuthorizationServerConfigurerCustomizer;
import org.ballcat.springsecurity.oauth2.server.authorization.config.configurer.OAuth2AuthorizationServerExtensionConfigurer;
import org.ballcat.springsecurity.oauth2.server.authorization.properties.OAuth2AuthorizationServerProperties;
import org.ballcat.springsecurity.oauth2.server.authorization.token.BallcatOAuth2TokenCustomizer;
import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2TokenRevocationResponseHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;

/**
 * OAuth2授权服务器自动配置类
 *
 * @author Hccake
 */
@Import({ AuthenticationManagerConfiguration.class, OAuth2AuthorizationServerConfigurerCustomizerConfiguration.class,
		OAuth2AuthorizationServerExtensionConfigurerConfiguration.class })
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableConfigurationProperties(OAuth2AuthorizationServerProperties.class)
public class OAuth2AuthorizationServerAutoConfiguration {

	/**
	 * OAuth2AuthorizationServerConfigurer 的适配器
	 * @param oAuth2AuthorizationServerConfigurerCustomizers
	 * OAuth2AuthorizationServerConfigurer 的定制器列表
	 * @param oAuth2AuthorizationServerExtensionConfigurers
	 * oAuth2AuthorizationServerExtensionConfigurer 的配置扩展器列表
	 * @return OAuth2AuthorizationServerConfigurerAdapter
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public OAuth2AuthorizationServerConfigurerAdapter oAuth2AuthorizationServerConfigurerAdapter(
			List<OAuth2AuthorizationServerConfigurerCustomizer> oAuth2AuthorizationServerConfigurerCustomizers,
			List<OAuth2AuthorizationServerExtensionConfigurer<?, HttpSecurity>> oAuth2AuthorizationServerExtensionConfigurers) {
		return new OAuth2AuthorizationServerConfigurerAdapter(oAuth2AuthorizationServerConfigurerCustomizers,
				oAuth2AuthorizationServerExtensionConfigurers);
	}

	/**
	 * OAuth2 授权服务器中注册的 client 仓库
	 */
	@Bean
	@ConditionalOnMissingBean
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
		return new JdbcRegisteredClientRepository(jdbcTemplate);
	}

	/**
	 * OAuth2 授权管理Service
	 */
	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository,
			ObjectProvider<OAuth2AuthorizationObjectMapperCustomizer> objectMapperCustomizerObjectProvider) {
		JdbcOAuth2AuthorizationService oAuth2AuthorizationService = new JdbcOAuth2AuthorizationService(jdbcTemplate,
				registeredClientRepository);

		// 需要注册自己的 mixin 来处理类型转换
		// link
		// https://github.com/spring-projects/spring-authorization-server/issues/397#issuecomment-900148920
		JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper rowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(
				registeredClientRepository);

		ObjectMapper objectMapper = new ObjectMapper();
		ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
		List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
		objectMapper.registerModules(securityModules);
		objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());

		// You will need to write the Mixin for your class so Jackson can marshall it.
		objectMapper.addMixIn(User.class, UserMixin.class);

		// 定制 objectMapper
		objectMapperCustomizerObjectProvider.ifAvailable(customizer -> customizer.customize(objectMapper));

		rowMapper.setObjectMapper(objectMapper);

		oAuth2AuthorizationService.setAuthorizationRowMapper(rowMapper);

		return oAuth2AuthorizationService;
	}

	/**
	 * OAuth2AuthorizationConsentService
	 */
	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}

	/**
	 * 授权服务器基本端点地址配置
	 * @return AuthorizationServerSettings
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

	/**
	 * OAuth2 Token 撤销响应处理器
	 * @param publisher 事件发布器
	 * @return OAuth2TokenRevocationResponseHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public OAuth2TokenRevocationResponseHandler oAuth2TokenRevocationResponseHandler(
			ApplicationEventPublisher publisher) {
		return new OAuth2TokenRevocationResponseHandler(publisher);
	}

	/**
	 * 密码管理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		return PasswordUtils.createDelegatingPasswordEncoder();
	}

	/**
	 * 对于使用不透明令牌的 client，需要存储对应的用户信息，以便在后续的请求中获取用户信息
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2TokenCustomizer.class)
	public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> oAuth2TokenCustomizer() {
		return new BallcatOAuth2TokenCustomizer();
	}

}
