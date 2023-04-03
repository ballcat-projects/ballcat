package org.ballcat.springsecurity.oauth2.server.authorization.config;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.oauth2.server.authorization.config.configurer.OAuth2AuthorizationServerExtensionConfigurer;
import org.ballcat.springsecurity.oauth2.server.authorization.config.customizer.OAuth2AuthorizationServerConfigurerCustomizer;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * OAuth2 的授权服务配置
 * <p>
 * 当实例既是授权服务器又是资源服务器时，Order 必须高于资源服务器
 *
 * @author hccake
 */
@RequiredArgsConstructor
@Order(99)
public class OAuth2AuthorizationServerConfigurerAdapter extends WebSecurityConfigurerAdapter {

	private final List<OAuth2AuthorizationServerConfigurerCustomizer> oAuth2AuthorizationServerConfigurerCustomizerList;

	private final List<OAuth2AuthorizationServerExtensionConfigurer<?, HttpSecurity>> oAuth2AuthorizationServerExtensionConfigurers;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 授权服务器配置
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
		for (OAuth2AuthorizationServerConfigurerCustomizer customizer : oAuth2AuthorizationServerConfigurerCustomizerList) {
			customizer.customize(authorizationServerConfigurer, http);
		}

		// @formatter:off
		RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
		http.requestMatchers()
				.requestMatchers(endpointsMatcher)
				.and()
				.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
				.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
				.apply(authorizationServerConfigurer);
		// @formatter:off

		for (OAuth2AuthorizationServerExtensionConfigurer<?, HttpSecurity> configurer : oAuth2AuthorizationServerExtensionConfigurers) {
			http.apply(configurer);
		}
	}


	protected final RequestMatcher getAuthenticationEntryPointMatcher(HttpSecurity http) {
		ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(ContentNegotiationStrategy.class);
		if (contentNegotiationStrategy == null) {
			contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
		}
		MediaTypeRequestMatcher mediaMatcher = new MediaTypeRequestMatcher(contentNegotiationStrategy,
				MediaType.APPLICATION_XHTML_XML, new MediaType("image", "*"), MediaType.TEXT_HTML,
				MediaType.TEXT_PLAIN);
		mediaMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
		RequestMatcher notXRequestedWith = new NegatedRequestMatcher(
				new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
		return new AndRequestMatcher(Arrays.asList(notXRequestedWith, mediaMatcher));
	}
}
