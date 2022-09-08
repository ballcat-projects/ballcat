package com.hccake.ballcat.auth.authentication;

import com.hccake.ballcat.common.security.userdetails.ClientPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

import java.util.HashMap;

/**
 * 客户端通过 basic 验证的处理逻辑
 *
 * @author hccake
 */
@Slf4j
@RequiredArgsConstructor
public class ClientBasicAuthenticationProvider implements AuthenticationProvider {

	private final ClientDetailsService clientDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) {
		UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;

		// mobile 登录只做了 app 用户的支持,所以这里就不判断客户端类型了
		String clientId = authenticationToken.getPrincipal().toString();
		ClientDetails clientDetails = null;

		try {
			clientDetails = clientDetailsService.loadClientByClientId(clientId);
		}
		catch (ClientRegistrationException ex) {
			throw new BadCredentialsException(ex.getMessage());
		}
		if (clientDetails == null) {
			log.debug("Authentication failed: no clientDetails find by clientId: {}", clientId);

			throw new BadCredentialsException(
					"ClientDetailsService returned null, which is an interface contract violation");
		}

		ClientPrincipal clientPrincipal = new ClientPrincipal(clientId, new HashMap<>(8),
				clientDetails.getAuthorities());
		clientPrincipal.setScope(clientDetails.getScope());

		return new OAuth2ClientAuthenticationToken(clientPrincipal, null);

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
