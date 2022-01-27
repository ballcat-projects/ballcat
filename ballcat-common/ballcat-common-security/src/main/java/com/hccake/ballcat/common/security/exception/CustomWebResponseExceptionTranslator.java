package com.hccake.ballcat.common.security.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * 覆盖默认的DefaultWebResponseExceptionTranslator的方法
 *
 * @author Hccake
 * @version 1.0
 * @date 2019/9/29 10:43
 */
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

	private final ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

	@Override
	public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
		// Try to extract a SpringSecurityException from the stacktrace
		Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);

		Exception ase = (InvalidGrantException) throwableAnalyzer.getFirstThrowableOfType(InvalidGrantException.class,
				causeChain);
		if (ase != null) {
			// 这里必须配置 messageSource，且指定的 basename 包含
			// ”org.springframework.security.messages“，否则错误信息没有国际化
			// {@link https://github.com/spring-projects/spring-security/issues/10227}
			CustomInvalidGrantException customInvalidGrantException = new CustomInvalidGrantException(ase.getMessage());
			return handleOAuth2Exception(customInvalidGrantException);
		}

		ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,
				causeChain);
		if (ase != null) {
			return handleOAuth2Exception(
					new CustomWebResponseExceptionTranslator.UnauthorizedException(e.getMessage(), e));
		}

		ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class,
				causeChain);
		if (ase != null) {
			return handleOAuth2Exception(
					new CustomWebResponseExceptionTranslator.ForbiddenException(ase.getMessage(), ase));
		}

		ase = (HttpRequestMethodNotSupportedException) throwableAnalyzer
				.getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
		if (ase != null) {
			return handleOAuth2Exception(new MethodNotAllowed(ase.getMessage(), ase));
		}

		// 放到最后，OAuth2 项目没有异常消息没有国际化
		ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);
		if (ase != null) {
			return handleOAuth2Exception((OAuth2Exception) ase);
		}

		return handleOAuth2Exception(new ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));
	}

	private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) {

		int status = e.getHttpErrorCode();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Cache-Control", "no-store");
		headers.set("Pragma", "no-cache");
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
		if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
			headers.set("WWW-Authenticate", String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
		}

		// 使用自定义的异常类进行包装
		return new ResponseEntity<>(new CustomOAuth2Exception(e.getMessage(), e), headers, HttpStatus.valueOf(status));

	}

	@SuppressWarnings("serial")
	private static class ForbiddenException extends CustomOAuth2Exception {

		public ForbiddenException(String msg, Throwable t) {
			super(msg, t);
		}

		@Override
		public String getOAuth2ErrorCode() {
			return "access_denied";
		}

		@Override
		public int getHttpErrorCode() {
			return 403;
		}

	}

	@SuppressWarnings("serial")
	private static class ServerErrorException extends CustomOAuth2Exception {

		public ServerErrorException(String msg, Throwable t) {
			super(msg, t);
		}

		@Override
		public String getOAuth2ErrorCode() {
			return "server_error";
		}

		@Override
		public int getHttpErrorCode() {
			return 500;
		}

	}

	@SuppressWarnings("serial")
	private static class UnauthorizedException extends CustomOAuth2Exception {

		public UnauthorizedException(String msg, Throwable t) {
			super(msg, t);
		}

		@Override
		public String getOAuth2ErrorCode() {
			return "unauthorized";
		}

		@Override
		public int getHttpErrorCode() {
			return 401;
		}

	}

	@SuppressWarnings("serial")
	private static class MethodNotAllowed extends CustomOAuth2Exception {

		public MethodNotAllowed(String msg, Throwable t) {
			super(msg, t);
		}

		@Override
		public String getOAuth2ErrorCode() {
			return "method_not_allowed";
		}

		@Override
		public int getHttpErrorCode() {
			return 405;
		}

	}

	@SuppressWarnings("serial")
	private static class CustomInvalidGrantException extends CustomOAuth2Exception {

		public CustomInvalidGrantException(String msg, Throwable t) {
			super(msg, t);
		}

		public CustomInvalidGrantException(String msg) {
			super(msg);
		}

		@Override
		public String getOAuth2ErrorCode() {
			return "invalid_grant";
		}

	}

}
