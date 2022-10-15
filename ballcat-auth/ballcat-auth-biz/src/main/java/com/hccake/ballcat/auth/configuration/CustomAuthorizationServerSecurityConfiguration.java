package com.hccake.ballcat.auth.configuration;

import com.hccake.ballcat.auth.OAuth2AuthorizationServerProperties;
import com.hccake.ballcat.auth.configurer.CustomAuthorizationServerSecurityConfigurer;
import com.hccake.ballcat.auth.filter.FilterWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configuration.ClientDetailsServiceConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

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

	private static final String DEFAULT_LOGIN_URL = "/login";

	@Autowired
	private OAuth2AuthorizationServerProperties oAuth2AuthorizationServerProperties;

	@Autowired(required = false)
	private UserDetailsService userDetailsService;

	@Autowired
	private List<FilterWrapper> filterWrappers;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Over-riding to make sure this.disableLocalConfigureAuthenticationBldr = false
		// This will ensure that when this configurer builds the AuthenticationManager it
		// will not attempt
		// to find another 'Global' AuthenticationManager in the ApplicationContext (if
		// available),
		// and set that as the parent of this 'Local' AuthenticationManager.
		// This AuthenticationManager should only be wired up with an
		// AuthenticationProvider
		// composed of the ClientDetailsService (wired in this configuration) for
		// authenticating 'clients' only.
	}

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
				.antMatchers(tokenEndpointPath, tokenKeyPath, checkTokenPath);
		// @formatter:on
		http.setSharedObject(ClientDetailsService.class, clientDetailsService);

		// 添加额外的过滤器
		for (FilterWrapper filterWrapper : filterWrappers) {
			http.addFilterAfter(filterWrapper.getFilter(), BasicAuthenticationFilter.class);
		}

		// 表单登录支持
		if (oAuth2AuthorizationServerProperties.isEnableFormLogin()) {
			String formLoginPage = oAuth2AuthorizationServerProperties.getFormLoginPage();

			HttpSecurity.RequestMatcherConfigurer requestMatcherConfigurer = http.requestMatchers();
			if (formLoginPage == null) {
				requestMatcherConfigurer.antMatchers(DEFAULT_LOGIN_URL);
				http.formLogin();
			}
			else {
				requestMatcherConfigurer.antMatchers(formLoginPage);
				http.formLogin(form -> form.loginPage(formLoginPage).permitAll());
			}

			// 需要 userDetailsService 对应生成 DaoAuthenticationProvider
			http.userDetailsService(this.userDetailsService);
		}

	}

	protected void configure(CustomAuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		for (ICustomAuthorizationServerConfigurer configurer : configurers) {
			configurer.configure(oauthServer);
		}
	}

}
