package com.hccake.ballcat.auth.configuration;

import com.hccake.ballcat.auth.configurer.CustomAuthorizationServerSecurityConfigurer;
import com.hccake.ballcat.auth.filter.FilterWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configuration.ClientDetailsServiceConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @deprecated See the <a href=
 * "https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth
 * 2.0 Migration Guide</a> for Spring Security 5.
 * @author hccake
 * @see AuthorizationServerSecurityConfiguration
 */
@Order(0)
@Import({ ClientDetailsServiceConfiguration.class, CustomAuthorizationServerEndpointsConfiguration.class })
@Deprecated
public class CustomAuthorizationServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private List<ICustomAuthorizationServerConfigurer> configurers = Collections.emptyList();

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private CustomAuthorizationServerEndpointsConfiguration endpoints;

	@Autowired
	public void configure(ClientDetailsServiceConfigurer clientDetails) throws Exception {
		for (ICustomAuthorizationServerConfigurer configurer : configurers) {
			configurer.configure(clientDetails);
		}
	}

	@Autowired(required = false)
	private UserDetailsService userDetailsService;

	@Autowired(required = false)
	private List<FilterWrapper> filterWrappers = new ArrayList<>();

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CustomAuthorizationServerSecurityConfigurer configurer = new CustomAuthorizationServerSecurityConfigurer();
		FrameworkEndpointHandlerMapping handlerMapping = endpoints.oauth2EndpointHandlerMapping();
		http.setSharedObject(FrameworkEndpointHandlerMapping.class, handlerMapping);
		configure(configurer);
		http.apply(configurer);
		String tokenEndpointPath = handlerMapping.getServletPath("/oauth/token");
		String tokenKeyPath = handlerMapping.getServletPath("/oauth/token_key");
		String checkTokenPath = handlerMapping.getServletPath("/oauth/check_token");
		if (!endpoints.getEndpointsConfigurer().isUserDetailsServiceOverride()) {
			UserDetailsService userDetailsService = http.getSharedObject(UserDetailsService.class);
			endpoints.getEndpointsConfigurer().userDetailsService(userDetailsService);
		}
		// @formatter:off
		http
				.authorizeRequests()
				.antMatchers(tokenEndpointPath).fullyAuthenticated()
				.antMatchers(tokenKeyPath).access(configurer.getTokenKeyAccess())
				.antMatchers(checkTokenPath).access(configurer.getCheckTokenAccess())
				.and()
				.requestMatchers()
				.antMatchers(tokenEndpointPath, tokenKeyPath, checkTokenPath)
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// @formatter:on
		http.setSharedObject(ClientDetailsService.class, clientDetailsService);

		// 添加额外的过滤器
		for (FilterWrapper filterWrapper : filterWrappers) {
			http.addFilterAfter(filterWrapper.getFilter(), BasicAuthenticationFilter.class);
		}

		// 需要 userDetailsService 对应生成 DaoAuthenticationProvider
		http.userDetailsService(this.userDetailsService);
	}

	protected void configure(CustomAuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		for (ICustomAuthorizationServerConfigurer configurer : configurers) {
			configurer.configure(oauthServer);
		}
	}

}
