package com.hccake.ballcat.common.security.oauth2.server.resource;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.common.security.userdetails.ClientPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * 共享存储的不透明令牌的处理器
 *
 * 当资源服务器与授权服务器共享了 TokenStore, 比如使用 RedisTokenStore 时共享了 redis，或者 jdbcTokenStore 时，共享了数据库。
 * 此时可以直接使用 spring-security-oauth2 中的 TokenStore 进行读取。
 *
 * @author hccake
 */
@Slf4j
@RequiredArgsConstructor
public class SharedStoredOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	private final TokenStore tokenStore;

	private static final String CLIENT_CREDENTIALS = "client_credentials";

	private static final String AUTHORITY_SCOPE_PREFIX = "SCOPE_";

	/**
	 * @see DefaultTokenServices#loadAuthentication(java.lang.String)
	 * @param accessTokenValue token
	 * @return OAuth2User
	 */
	@Override
	public OAuth2AuthenticatedPrincipal introspect(String accessTokenValue) {
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
		if (accessToken == null) {
			throw new BadOpaqueTokenException("Invalid access token: " + accessTokenValue);
		}
		else if (accessToken.isExpired()) {
			tokenStore.removeAccessToken(accessToken);
			throw new BadOpaqueTokenException("Access token expired: " + accessTokenValue);
		}

		OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
		if (oAuth2Authentication == null) {
			// in case of race condition
			throw new BadOpaqueTokenException("Invalid access token: " + accessTokenValue);
		}

		ClientPrincipal clientPrincipal = getClientPrincipal(oAuth2Authentication);
		if (clientPrincipal != null) {
			return clientPrincipal;
		}

		return (OAuth2User) oAuth2Authentication.getPrincipal();
	}

	private ClientPrincipal getClientPrincipal(OAuth2Authentication oAuth2Authentication) {
		ClientPrincipal clientPrincipal = null;

		OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
		if (oAuth2Request != null && CLIENT_CREDENTIALS.equals(oAuth2Request.getGrantType())) {
			Collection<? extends GrantedAuthority> requestAuthorities = oAuth2Request.getAuthorities();
			Collection<GrantedAuthority> authorities = new ArrayList<>(requestAuthorities);
			Set<String> scopes = oAuth2Request.getScope();
			if (CollectionUtil.isNotEmpty(scopes)) {
				for (String scope : scopes) {
					authorities.add(new SimpleGrantedAuthority(AUTHORITY_SCOPE_PREFIX + scope));
				}
			}
			clientPrincipal = new ClientPrincipal(oAuth2Request.getClientId(), new HashMap<>(8), authorities);
			clientPrincipal.setScope(scopes);
		}

		return clientPrincipal;
	}

}
