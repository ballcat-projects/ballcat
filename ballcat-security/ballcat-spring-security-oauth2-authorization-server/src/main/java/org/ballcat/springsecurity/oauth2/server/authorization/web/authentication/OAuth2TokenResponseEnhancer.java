package org.ballcat.springsecurity.oauth2.server.authorization.web.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

import java.util.Map;

/**
 * Customize additional parameters for OAuth2 Token Response
 *
 * @see org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
 * @author hccake
 */
@FunctionalInterface
public interface OAuth2TokenResponseEnhancer {

	/**
	 * Provide an additional parameter map to enhance OAuth2 Token Response
	 * @param accessTokenAuthentication An {@link Authentication} implementation used when
	 * issuing an * OAuth 2.0 Access Token and (optional) Refresh Token.
	 * @return an additional parameter map
	 */
	Map<String, Object> enhance(OAuth2AccessTokenAuthenticationToken accessTokenAuthentication);

}