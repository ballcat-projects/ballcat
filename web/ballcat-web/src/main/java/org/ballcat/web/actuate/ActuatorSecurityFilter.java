/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.web.actuate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ballcat.common.core.constant.HeaderConstants;
import org.ballcat.common.model.result.ApiResult;
import org.ballcat.common.model.result.SystemResultCode;
import org.ballcat.common.util.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Actuator 安全过滤器，做一个签名认证，校验通过才允许访问
 *
 * @author Hccake 2019/10/17 20:28
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
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			ApiResult<String> apiResult = ApiResult.failed(SystemResultCode.UNAUTHORIZED);
			response.getWriter().write(JsonUtils.toJson(apiResult));
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
		if (StringUtils.hasText(sign) && StringUtils.hasText(reqTime) && StringUtils.hasText(reqSecretId)) {
			if (!reqSecretId.equals(this.secretId)) {
				return false;
			}
			// 过期时间 30秒失效
			long expireTime = 30 * 1000L;
			long nowTime = System.currentTimeMillis();
			if (nowTime - Long.parseLong(reqTime) <= expireTime) {
				String reverse = new StringBuilder(reqTime).reverse().toString();
				String checkSign = DigestUtils
					.md5DigestAsHex((reverse + this.secretId + this.secretKey).getBytes(StandardCharsets.UTF_8));
				return checkSign.equalsIgnoreCase(sign);
			}
		}
		return false;
	}

}
