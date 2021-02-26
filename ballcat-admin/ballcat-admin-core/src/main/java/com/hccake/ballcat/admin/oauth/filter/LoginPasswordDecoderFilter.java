package com.hccake.ballcat.admin.oauth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.admin.constants.SecurityConst;
import com.hccake.ballcat.admin.oauth.util.SecurityUtils;
import com.hccake.ballcat.common.core.request.wrapper.ModifyParamMapRequestWrapper;
import com.hccake.ballcat.common.core.result.R;
import com.hccake.ballcat.common.core.result.SystemResultCode;
import com.hccake.ballcat.common.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/28 16:57 前端传递过来的加密密码，需要在登陆之前先解密
 */
@Slf4j
@Order(0)
@WebFilter(urlPatterns = { SecurityConst.LOGIN_URL })
@RequiredArgsConstructor
public class LoginPasswordDecoderFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Value("${ballcat.password.secret-key}")
	private String secretKey;

	private static final String PASSWORD = "password";

	private static final String GRANT_TYPE = "grant_type";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 测试客户端 跳过密码解密（swagger 或 postman测试时使用）
		if (SecurityUtils.isTestClient()) {
			filterChain.doFilter(request, response);
			return;
		}

		// 解密前台加密后的密码
		Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
		try {
			if (request.getParameter(GRANT_TYPE).equals(PASSWORD)) {
				String password = PasswordUtils.decodeAES(request.getParameter(PASSWORD), secretKey);
				parameterMap.put(PASSWORD, new String[] { password });
			}
		}
		catch (Exception e) {
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON.toString());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter()
					.write(objectMapper.writeValueAsString(R.failed(SystemResultCode.UNAUTHORIZED, e.getMessage())));
			return;
		}

		// SpringSecurity 默认从ParameterMap中获取密码参数
		// 由于原生的request中对parameter加锁了，无法修改，所以使用包装类
		filterChain.doFilter(new ModifyParamMapRequestWrapper(request, parameterMap), response);
	}

}
