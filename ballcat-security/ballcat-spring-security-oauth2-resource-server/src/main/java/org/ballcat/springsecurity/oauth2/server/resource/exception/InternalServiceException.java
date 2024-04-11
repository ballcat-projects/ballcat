package org.ballcat.springsecurity.oauth2.server.resource.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 认证时发生内部服务异常。
 * <p>
 *
 * @see org.springframework.security.authentication.InternalAuthenticationServiceException
 * @author Hccake
 */
public class InternalServiceException extends AuthenticationException {

	public InternalServiceException(String message) {
		super(message);
	}

	public InternalServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
