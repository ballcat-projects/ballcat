package com.hccake.ballcat.autoconfigure.web.actuate;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.hccake.ballcat.common.core.constant.HeaderConstants;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import com.hccake.ballcat.common.util.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * The type Actuator filter.
 *
 * @author Hccake
 * @version 1.0
 * @date 2019 /10/17 20:28
 */
public class ActuatorSecurityFilter extends OncePerRequestFilter {

	private final String secretId;

	private final String secretKey;

	/**
	 * Instantiates a new Actuator filter.
	 * @param secretId the secret id
	 * @param secretKey the secret key
	 */
	public ActuatorSecurityFilter(String secretId, String secretKey) {
		this.secretId = secretId;
		this.secretKey = secretKey;
	}

	/**
	 * Same contract as for {@code doFilter}, but guaranteed to be just invoked once per
	 * request within a single request thread. See {@link #shouldNotFilterAsyncDispatch()}
	 * for details.
	 * <p>
	 * Provides HttpServletRequest and HttpServletResponse arguments instead of the
	 * default ServletRequest and ServletResponse ones.
	 * @param request 请求信息
	 * @param response 响应信息
	 * @param filterChain 过滤器链
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 检验签名是否正确
		String reqSecretId = request.getHeader(HeaderConstants.SECRET_ID);
		String sign = request.getHeader(HeaderConstants.SIGN);
		String reqTime = request.getHeader(HeaderConstants.REQ_TIME);
		if (verifySign(reqSecretId, sign, reqTime)) {
			filterChain.doFilter(request, response);
		}
		else {
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON.toString());
			response.setHeader("Accept-Charset", StandardCharsets.UTF_8.toString());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			R<String> r = R.failed(SystemResultCode.UNAUTHORIZED);
			response.getWriter().write(JsonUtils.toJson(r));
		}
	}

	/**
	 * 校验sign
	 * @param reqSecretId secretId
	 * @param sign 签名
	 * @param reqTime 请求时间戳 ms
	 * @return boolean 通过返回true
	 */
	private boolean verifySign(String reqSecretId, String sign, String reqTime) {
		if (StrUtil.isNotBlank(sign) && StrUtil.isNotBlank(reqTime) && StrUtil.isNotBlank(reqSecretId)) {
			if (!reqSecretId.equals(this.secretId)) {
				return false;
			}
			// 过期时间 30秒失效
			long expireTime = 30 * 1000L;
			long nowTime = System.currentTimeMillis();
			if (nowTime - Long.parseLong(reqTime) <= expireTime) {
				String reverse = StrUtil.reverse(reqTime);
				String checkSign = SecureUtil.md5(reverse + this.secretId + this.secretKey);
				return StrUtil.equalsIgnoreCase(checkSign, sign);
			}
		}
		return false;
	}

}
