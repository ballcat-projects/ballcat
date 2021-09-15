package com.hccake.ballcat.auth.authentication;

import com.hccake.ballcat.common.security.userdetails.ClientPrincipal;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.HashMap;

/**
 * client_credentials 客户端凭证模式的授权处理器
 *
 * @author hccake
 */
public class CustomClientCredentialsTokenGranter extends ClientCredentialsTokenGranter {

	public CustomClientCredentialsTokenGranter(AuthorizationServerTokenServices tokenServices,
			ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
		super(tokenServices, clientDetailsService, requestFactory);
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

		OAuth2Request oAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);

		ClientPrincipal clientPrincipal = new ClientPrincipal(oAuth2Request.getClientId(), new HashMap<>(8),
				client.getAuthorities());
		clientPrincipal.setScope(client.getScope());

		OAuth2ClientAuthenticationToken userAuthentication = new OAuth2ClientAuthenticationToken(clientPrincipal, null);

		return new OAuth2Authentication(oAuth2Request, userAuthentication);

	}

}
