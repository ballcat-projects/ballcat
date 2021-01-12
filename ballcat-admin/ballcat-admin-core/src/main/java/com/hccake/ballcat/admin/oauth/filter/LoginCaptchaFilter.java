package com.hccake.ballcat.admin.oauth.filter;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.common.core.result.R;
import com.hccake.ballcat.common.core.result.SystemResultCode;
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

	private final ObjectMapper objectMapper;

	private final CaptchaService captchaService;

	private final static String CAPTCHA_VERIFICATION_PARAM = "captchaVerification";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String captchaVerification = request.getParameter(CAPTCHA_VERIFICATION_PARAM);

		CaptchaVO captchaVO = new CaptchaVO();
		captchaVO.setCaptchaVerification(captchaVerification);
		ResponseModel responseModel = captchaService.verification(captchaVO);

		if (responseModel.isSuccess()) {
			filterChain.doFilter(request, response);
		}
		else {
			// 验证码校验失败，返回信息告诉前端
			// repCode 0000 无异常，代表成功
			// repCode 9999 服务器内部异常
			// repCode 0011 参数不能为空
			// repCode 6110 验证码已失效，请重新获取
			// repCode 6111 验证失败
			// repCode 6112 获取验证码失败,请联系管理员
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON.toString());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write(
					objectMapper.writeValueAsString(R.failed(SystemResultCode.UNAUTHORIZED, "Captcha code error")));
		}

	}

}
