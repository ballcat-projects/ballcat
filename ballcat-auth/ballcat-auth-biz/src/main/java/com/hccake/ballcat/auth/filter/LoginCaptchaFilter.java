package com.hccake.ballcat.auth.filter;

import cn.hutool.core.text.CharSequenceUtil;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import com.hccake.ballcat.common.security.ScopeNames;
import com.hccake.ballcat.common.security.userdetails.ClientPrincipal;
import com.hccake.ballcat.common.security.util.SecurityUtils;
import com.hccake.ballcat.common.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.ballcat.security.captcha.CaptchaValidateResult;
import org.ballcat.security.captcha.CaptchaValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Hccake 2021/1/11
 * @version 1.0
 */
@Deprecated
@RequiredArgsConstructor
public class LoginCaptchaFilter extends OncePerRequestFilter {

	private final CaptchaValidator captchaValidator;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 只对 password 的 grant_type 进行拦截处理
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 测试客户端 跳过验证码（swagger 或 postman测试时使用）
		ClientPrincipal clientPrincipal = SecurityUtils.getClientPrincipal();
		if (clientPrincipal != null && clientPrincipal.getScope().contains(ScopeNames.SKIP_CAPTCHA)) {
			filterChain.doFilter(request, response);
			return;
		}

		CaptchaValidateResult captchaValidateResult = captchaValidator.validate(request);
		if (captchaValidateResult.isSuccess()) {
			filterChain.doFilter(request, response);
		}
		else {
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			R<String> r = R.failed(SystemResultCode.UNAUTHORIZED,
					CharSequenceUtil.blankToDefault(captchaValidateResult.getMessage(), "Captcha code error"));
			response.getWriter().write(JsonUtils.toJson(r));
		}

	}

}
