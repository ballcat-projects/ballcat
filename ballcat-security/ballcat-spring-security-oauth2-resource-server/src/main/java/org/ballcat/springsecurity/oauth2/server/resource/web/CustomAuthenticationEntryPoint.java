package org.ballcat.springsecurity.oauth2.server.resource.web;

import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import com.hccake.ballcat.common.util.JsonUtils;
import org.ballcat.springsecurity.oauth2.server.resource.exception.InternalServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Hccake 2019/9/25 22:04
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AuthenticationException e) throws IOException, ServletException {

		String utf8 = StandardCharsets.UTF_8.toString();

		httpServletResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		httpServletResponse.setCharacterEncoding(utf8);

		if (e instanceof InternalServiceException) {
			httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			R<Object> r = R.failed(SystemResultCode.SERVER_ERROR, e.getMessage());
			httpServletResponse.getWriter().write(JsonUtils.toJson(r));
		}
		else {
			httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
			R<Object> r = R.failed(SystemResultCode.UNAUTHORIZED, e.getMessage());
			httpServletResponse.getWriter().write(JsonUtils.toJson(r));
		}

	}

}
