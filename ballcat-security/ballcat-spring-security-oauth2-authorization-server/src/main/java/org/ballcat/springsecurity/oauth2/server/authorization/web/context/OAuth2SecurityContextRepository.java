package org.ballcat.springsecurity.oauth2.server.authorization.web.context;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * OAuth2 下使用的 SecurityContextRepository，从 bearerTokenResolver 中解析对应的 access_token, 然后利用
 * oAuth2AuthorizationService 来获取对应的 SecurityContext
 *
 * @author hccake
 */
public class OAuth2SecurityContextRepository implements SecurityContextRepository {

	private final BearerTokenResolver bearerTokenResolver;

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	public OAuth2SecurityContextRepository(OAuth2AuthorizationService oAuth2AuthorizationService) {
		DefaultBearerTokenResolver tokenResolver = new DefaultBearerTokenResolver();
		// 允许 url 携带 accessToken
		tokenResolver.setAllowUriQueryParameter(true);
		// 允许 表单传参
		tokenResolver.setAllowFormEncodedBodyParameter(true);
		this.bearerTokenResolver = tokenResolver;
		this.oAuth2AuthorizationService = oAuth2AuthorizationService;
	}

	public OAuth2SecurityContextRepository(BearerTokenResolver bearerTokenResolver,
			OAuth2AuthorizationService oAuth2AuthorizationService) {
		this.bearerTokenResolver = bearerTokenResolver;
		this.oAuth2AuthorizationService = oAuth2AuthorizationService;
	}

	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		HttpServletRequest request = requestResponseHolder.getRequest();
		String bearerToken = this.bearerTokenResolver.resolve(request);
		if (!StringUtils.hasText(bearerToken)) {
			return SecurityContextHolder.createEmptyContext();
		}
		OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(bearerToken,
				OAuth2TokenType.ACCESS_TOKEN);
		if (oAuth2Authorization == null) {
			return SecurityContextHolder.createEmptyContext();
		}
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) oAuth2Authorization
				.getAttributes().get("java.security.Principal");
		return new SecurityContextImpl(usernamePasswordAuthenticationToken);
	}

	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {

	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
		return this.loadContext(request) != null;
	}

}
