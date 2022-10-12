package org.ballcat.springsecurity.oauth2.server.authorization.web.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * OAuth2 撤销令牌成功的处理，发布登出事件，以及响应 200
 *
 * @author hccake
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc7009#section-2">Section 2
 * Token Revocation</a>
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc7009#section-2.1">Section
 * 2.1 Revocation Request</a>
 */
@RequiredArgsConstructor
public class OAuth2TokenRevocationResponseHandler implements AuthenticationSuccessHandler {

	private final ApplicationEventPublisher publisher;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		// 发布用户登出事件
		publisher.publishEvent(new LogoutSuccessEvent(authentication));
		// 返回 200 响应
		response.setStatus(HttpStatus.OK.value());
	}

}
