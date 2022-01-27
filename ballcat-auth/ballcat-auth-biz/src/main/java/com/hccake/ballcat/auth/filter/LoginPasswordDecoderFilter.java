package com.hccake.ballcat.auth.filter;

import com.hccake.ballcat.common.core.request.wrapper.ModifyParamMapRequestWrapper;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import com.hccake.ballcat.common.security.util.PasswordUtils;
import com.hccake.ballcat.common.security.util.SecurityUtils;
import com.hccake.ballcat.common.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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

	private final String passwordSecretKey;

	private static final String PASSWORD = "password";

	private static final String GRANT_TYPE = "grant_type";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 未配置密码密钥时，直接跳过
		if (passwordSecretKey == null) {
			log.warn("passwordSecretKey not configured, skip password decoder");
			filterChain.doFilter(request, response);
			return;
		}

		// 测试客户端 跳过密码解密（swagger 或 postman测试时使用）
		if (SecurityUtils.isTestClient()) {
			filterChain.doFilter(request, response);
			return;
		}

		// 解密前台加密后的密码
		Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
		String passwordAes = request.getParameter(PASSWORD);

		try {
			if (request.getParameter(GRANT_TYPE).equals(PASSWORD)) {
				String password = PasswordUtils.decodeAES(passwordAes, passwordSecretKey);
				parameterMap.put(PASSWORD, new String[] { password });
			}
		}
		catch (Exception e) {
			log.error("[doFilterInternal] password decode aes error，passwordAes: {}，passwordSecretKey: {}", passwordAes,
					passwordSecretKey, e);
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON.toString());
			response.setHeader("Accept-Charset", StandardCharsets.UTF_8.toString());
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
