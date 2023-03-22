package org.ballcat.springsecurity.oauth2.server.resource.introspection;

import cn.hutool.core.collection.CollUtil;
import com.hccake.ballcat.common.security.userdetails.ClientPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * 共享存储的不透明令牌的处理器
 * <p>
 * 当资源服务器与授权服务器共享了 TokenStore, 比如使用 RedisTokenStore 时共享了 redis，或者 jdbcTokenStore 时，共享了数据库。
 * 此时可以直接使用 spring-authorization-server 中的 OAuth2AuthorizationService 进行读取。
 *
 * @author hccake
 */
@Slf4j
public class SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	private static final String AUTHORITY_SCOPE_PREFIX = "SCOPE_";

	private final OAuth2AuthorizationService authorizationService;

	public SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector(
			OAuth2AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	/**
	 * @param accessTokenValue token
	 * @return OAuth2AuthenticatedPrincipal
	 */
	@Override
	public OAuth2AuthenticatedPrincipal introspect(String accessTokenValue) {
		OAuth2Authorization authorization = authorizationService.findByToken(accessTokenValue, null);
		if (authorization == null) {
			if (log.isTraceEnabled()) {
				log.trace("Did not authenticate token introspection request since token was not found");
			}
			throw new BadOpaqueTokenException("Invalid access token: " + accessTokenValue);
		}

		OAuth2Authorization.Token<OAuth2Token> authorizedToken = authorization.getToken(accessTokenValue);
		if (authorizedToken == null || !authorizedToken.isActive()) {
			if (log.isTraceEnabled()) {
				log.trace("Did not validate token since it is inactive");
			}
			throw new BadOpaqueTokenException("Provided token isn't active");
		}

		AuthorizationGrantType authorizationGrantType = authorization.getAuthorizationGrantType();
		if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(authorizationGrantType)) {
			return getClientPrincipal(authorization);
		}

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authorization
			.getAttributes()
			.get("java.security.Principal");
		return (OAuth2AuthenticatedPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
	}

	private ClientPrincipal getClientPrincipal(OAuth2Authorization oAuth2Authentication) {
		ClientPrincipal clientPrincipal;

		Set<String> authorizedScopes = oAuth2Authentication.getAuthorizedScopes();

		Collection<GrantedAuthority> authorities = new ArrayList<>();
		if (CollUtil.isNotEmpty(authorizedScopes)) {
			for (String scope : authorizedScopes) {
				authorities.add(new SimpleGrantedAuthority(AUTHORITY_SCOPE_PREFIX + scope));
			}
		}
		clientPrincipal = new ClientPrincipal(oAuth2Authentication.getPrincipalName(), new HashMap<>(8), authorities);
		clientPrincipal.setScope(authorizedScopes);

		return clientPrincipal;
	}

}
