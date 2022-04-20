package com.hccake.ballcat.auth.filter;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.auth.filter.captcha.CaptchaValidator;
import com.hccake.ballcat.auth.filter.captcha.domain.CaptchaResponse;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import com.hccake.ballcat.common.security.util.SecurityUtils;
import com.hccake.ballcat.common.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequiredArgsConstructor
public class LoginCaptchaFilter extends OncePerRequestFilter {

	private final CaptchaValidator captchaValidator;

	private static final String GRANT_TYPE_PASSWORD = "password";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 测试客户端 跳过验证码（swagger 或 postman测试时使用）
		if (SecurityUtils.isTestClient()) {
			filterChain.doFilter(request, response);
			return;
		}

		// 只对 password 的 grant_type 进行拦截处理
		String grantType = request.getParameter("grant_type");
		if (!GRANT_TYPE_PASSWORD.equals(grantType)) {
			filterChain.doFilter(request, response);
			return;
		}

		CaptchaResponse captchaResponse = captchaValidator.validate(request);
		if (captchaResponse.isSuccess()) {
			filterChain.doFilter(request, response);
		}
		else {
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON.toString());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			R<String> r = R.failed(SystemResultCode.UNAUTHORIZED,
					StrUtil.blankToDefault(captchaResponse.getErrMsg(), "Captcha code error"));
			response.getWriter().write(JsonUtils.toJson(r));
		}

	}

}
