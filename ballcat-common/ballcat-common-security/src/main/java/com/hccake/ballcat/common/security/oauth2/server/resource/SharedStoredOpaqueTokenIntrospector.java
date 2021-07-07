package com.hccake.ballcat.common.security.oauth2.server.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

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

	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
		if (oAuth2Authentication == null) {
			log.trace("Could not be retrieved from the token store");
			throw new BadOpaqueTokenException("Provided token isn't active");
		}
		return (OAuth2User) oAuth2Authentication.getPrincipal();
	}

}
