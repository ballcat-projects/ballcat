package com.hccake.ballcat.admin.oauth.mobile;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/1/20 18:18
 */
public class MobileTokenGranter extends AbstractTokenGranter {

	private static final String GRANT_TYPE = "mobile";

	private final AuthenticationManager authenticationManager;

	public MobileTokenGranter(AuthenticationManager authenticationManager,
			AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
			OAuth2RequestFactory requestFactory) {
		this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);

	}

	protected MobileTokenGranter(AuthenticationManager authenticationManager,
			AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
			OAuth2RequestFactory requestFactory, String grantType) {
		super(tokenServices, clientDetailsService, requestFactory, grantType);
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
		Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
		String mobile = parameters.get("mobile");
		if (mobile == null) {
			mobile = "";
		}
		mobile = mobile.trim();

		MobileAuthenticationToken mobileAuthenticationToken = new MobileAuthenticationToken(mobile);

		Authentication authentication = null;
		try {
			authentication = this.authenticationManager.authenticate(mobileAuthenticationToken);

			logger.debug("Authentication success: " + authentication);
			SecurityContextHolder.getContext().setAuthentication(authentication);

		}
		catch (Exception failed) {
			SecurityContextHolder.clearContext();
			logger.debug("Authentication request failed: " + failed);

			// eventPublisher.publishAuthenticationFailure(new
			// BadCredentialsException(failed.getMessage(), failed),
			// new PreAuthenticatedAuthenticationToken("access-token", "N/A"));

			// try {
			// authenticationEntryPoint.commence(request, response,
			// new UsernameNotFoundException(failed.getMessage(), failed));
			// } catch (Exception e) {
			// logger.error("authenticationEntryPoint handle error:{}", failed);
			// }
		}

		OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
		return new OAuth2Authentication(storedOAuth2Request, authentication);
	}

}
