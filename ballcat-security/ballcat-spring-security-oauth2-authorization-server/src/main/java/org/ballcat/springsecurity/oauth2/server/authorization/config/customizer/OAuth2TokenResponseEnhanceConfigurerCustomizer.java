package org.ballcat.springsecurity.oauth2.server.authorization.config.customizer;

import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2TokenResponseEnhancer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * OAuth2 Token 响应增强配置
 *
 * @author chengbohua
 */
public class OAuth2TokenResponseEnhanceConfigurerCustomizer implements OAuth2AuthorizationServerConfigurerCustomizer {

	private final OAuth2TokenResponseEnhancer oauth2TokenResponseEnhancer;

	private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter;

	private final boolean setAccessTokenCookie;

	public OAuth2TokenResponseEnhanceConfigurerCustomizer(OAuth2TokenResponseEnhancer oauth2TokenResponseEnhancer) {
		this(oauth2TokenResponseEnhancer, new OAuth2AccessTokenResponseHttpMessageConverter());
	}

	public OAuth2TokenResponseEnhanceConfigurerCustomizer(OAuth2TokenResponseEnhancer oauth2TokenResponseEnhancer,
			HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter) {
		this(oauth2TokenResponseEnhancer, accessTokenHttpResponseConverter, false);
	}

	public OAuth2TokenResponseEnhanceConfigurerCustomizer(OAuth2TokenResponseEnhancer oauth2TokenResponseEnhancer,
			boolean setAccessTokenCookie) {
		this(oauth2TokenResponseEnhancer, new OAuth2AccessTokenResponseHttpMessageConverter(), setAccessTokenCookie);
	}

	public OAuth2TokenResponseEnhanceConfigurerCustomizer(OAuth2TokenResponseEnhancer oauth2TokenResponseEnhancer,
			HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter,
			boolean setAccessTokenCookie) {
		this.oauth2TokenResponseEnhancer = oauth2TokenResponseEnhancer;
		this.accessTokenHttpResponseConverter = accessTokenHttpResponseConverter;
		this.setAccessTokenCookie = setAccessTokenCookie;
	}

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) {
		oAuth2AuthorizationServerConfigurer.tokenEndpoint(oAuth2TokenEndpointConfigurer -> oAuth2TokenEndpointConfigurer
			.accessTokenResponseHandler(this::sendAccessTokenResponse));
	}

	private void sendAccessTokenResponse(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

		OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
		OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();

		OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
			.tokenType(accessToken.getTokenType())
			.scopes(accessToken.getScopes());
		if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
			builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
		}
		if (refreshToken != null) {
			builder.refreshToken(refreshToken.getTokenValue());
		}
		Map<String, Object> additionalParameters = oauth2TokenResponseEnhancer.enhance(accessTokenAuthentication);
		if (!CollectionUtils.isEmpty(additionalParameters)) {
			builder.additionalParameters(additionalParameters);
		}
		OAuth2AccessTokenResponse accessTokenResponse = builder.build();
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

		// 添加 cookie, 配合无状态登陆使用
		if (setAccessTokenCookie) {
			setCookie(request, response, OAuth2TokenType.ACCESS_TOKEN.getValue(), accessToken.getTokenValue(), 86400);
		}

		this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
	}

	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value,
			int maxAge) {
		final Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath(getRequestContext(request));
		cookie.setSecure(request.isSecure());
		cookie.setHttpOnly(true);

		response.addCookie(cookie);
	}

	private static String getRequestContext(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		return (contextPath.length() > 0) ? contextPath : "/";
	}

}
