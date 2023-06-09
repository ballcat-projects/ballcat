package org.ballcat.security.web.filter;

import lombok.RequiredArgsConstructor;
import org.ballcat.security.SecurityToken;
import org.ballcat.security.resources.SecurityResourceService;
import org.ballcat.security.resources.SecurityScope;
import org.ballcat.security.web.constant.SecurityWebConstants;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lingting 2023-03-29 21:18
 */
@RequiredArgsConstructor
public class SecurityWebResourceFilter extends OncePerRequestFilter {

	private final SecurityResourceService service;

	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
			@NotNull FilterChain filterChain) throws ServletException, IOException {

		SecurityToken token = getTokenHeader(request);
		if (StringUtils.hasText(token.getToken())) {
			SecurityScope scope = service.resolve(token);
			service.setScope(scope);
		}
		try {
			filterChain.doFilter(request, response);
		}
		finally {
			service.removeScope();
		}
	}

	public SecurityToken getTokenHeader(HttpServletRequest request) {
		String raw = request.getHeader(SecurityWebConstants.TOKEN_HEADER);

		// 走参数
		if (!StringUtils.hasText(raw)) {
			return getTokenByParams(request);
		}
		SecurityToken token = new SecurityToken();

		String[] split = raw.split(SecurityWebConstants.TOKEN_DELIMITER, 2);

		token.setRaw(raw);

		if (split.length > 1) {
			token.setType(split[0]);
			token.setToken(split[1]);
		}
		else {
			token.setType(null);
			token.setToken(split[0]);
		}

		return token;
	}

	public SecurityToken getTokenByParams(HttpServletRequest request) {
		SecurityToken token = new SecurityToken();
		String value = request.getParameter(SecurityWebConstants.TOKEN_PARAMETER);
		token.setType(SecurityWebConstants.TOKEN_TYPE_BEARER);
		token.setToken(value);
		token.setRaw(value);
		return token;
	}

}
