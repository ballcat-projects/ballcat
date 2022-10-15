package com.hccake.ballcat.auth.authentication;

import com.hccake.ballcat.common.security.userdetails.ClientPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import java.util.HashMap;

/**
 * 客户端通过 basic 验证的处理逻辑
 *
 * @author hccake
 */
@Slf4j
@Deprecated
@RequiredArgsConstructor
public class ClientBasicAuthenticationProvider implements AuthenticationProvider {

	private final ClientDetailsService clientDetailsService;

	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String clientId = authentication.getPrincipal().toString();

		ClientDetails clientDetails;
		try {
			clientDetails = clientDetailsService.loadClientByClientId(clientId);
		}
		catch (NoSuchClientException ex) {
			throw new UsernameNotFoundException(ex.getMessage(), ex);
		}

		// client id 和 secret 校验
		String presentedPassword = authentication.getCredentials().toString();
		if (!this.passwordEncoder.matches(presentedPassword, clientDetails.getClientSecret())) {
			log.debug("Failed to authenticate since password does not match stored value");
			throw new BadCredentialsException("error client id or secret");
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
