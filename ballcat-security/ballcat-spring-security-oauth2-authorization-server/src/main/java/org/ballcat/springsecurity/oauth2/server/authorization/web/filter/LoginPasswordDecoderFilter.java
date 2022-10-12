package org.ballcat.springsecurity.oauth2.server.authorization.web.filter;

import com.hccake.ballcat.common.core.request.wrapper.ModifyParamMapRequestWrapper;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import com.hccake.ballcat.common.security.ScopeNames;
import com.hccake.ballcat.common.security.util.PasswordUtils;
import com.hccake.ballcat.common.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.ballcat.springsecurity.oauth2.server.authorization.authentication.OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient;

/**
 * 前端传递过来的加密密码，需要在登陆之前先解密
 *
 * @author Hccake
 * @version 1.0
 * @date 2019/9/28 16:57
 */
@Slf4j
@RequiredArgsConstructor
public class LoginPasswordDecoderFilter extends OncePerRequestFilter {

	private final RequestMatcher requestMatcher;

	private final String passwordSecretKey;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!this.requestMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 未配置密码密钥时，直接跳过
		if (passwordSecretKey == null) {
			log.warn("passwordSecretKey not configured, skip password decoder");
			filterChain.doFilter(request, response);
			return;
		}

		// 非密码模式下，直接跳过
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 获取当前客户端
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(authentication);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		// 测试客户端密码不加密，直接跳过（swagger 或 postman测试时使用）
		if (registeredClient != null && registeredClient.getScopes().contains(ScopeNames.SKIP_PASSWORD_DECODE)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 解密前台加密后的密码
		Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
		String passwordAes = request.getParameter(OAuth2ParameterNames.PASSWORD);

		try {
			String password = PasswordUtils.decodeAES(passwordAes, passwordSecretKey);
			parameterMap.put(OAuth2ParameterNames.PASSWORD, new String[] { password });
		}
		catch (Exception e) {
			log.error("[doFilterInternal] password decode aes error，passwordAes: {}，passwordSecretKey: {}", passwordAes,
					passwordSecretKey, e);
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			R<String> r = R.failed(SystemResultCode.UNAUTHORIZED, "用户名或密码错误！");
			response.getWriter().write(JsonUtils.toJson(r));
			return;
		}

		// SpringSecurity 默认从ParameterMap中获取密码参数
		// 由于原生的request中对parameter加锁了，无法修改，所以使用包装类
		filterChain.doFilter(new ModifyParamMapRequestWrapper(request, parameterMap), response);
	}

}
