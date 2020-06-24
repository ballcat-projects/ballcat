package com.hccake.ballcat.admin.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.core.result.R;
import com.hccake.ballcat.common.core.result.SystemResultCode;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hccake
 */
@RestController
@RequestMapping("/oauth")
@Api(value = "oauth", tags = "用户认证模块")
@RequiredArgsConstructor
public class AuthController {

	private final TokenStore tokenStore;

	private final ApplicationEventPublisher publisher;

	/**
	 * 退出token
	 * @param authHeader Authorization
	 */
	@DeleteMapping("/logout")
	public R<?> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
		if (StrUtil.isBlank(authHeader)) {
			return R.failed(SystemResultCode.FORBIDDEN, "退出失败，token 为空");
		}

		String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StrUtil.EMPTY).trim();
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
		if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
			return R.failed(SystemResultCode.FORBIDDEN, "退出失败，token 无效");
		}

		// TODO 清空用户缓存信息
		OAuth2Authentication auth2Authentication = tokenStore.readAuthentication(accessToken);
		String username = auth2Authentication.getName();

		// 清空access token
		tokenStore.removeAccessToken(accessToken);
		// 清空 refresh token
		OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
		tokenStore.removeRefreshToken(refreshToken);

		// 发布用户登出事件
		publisher.publishEvent(new LogoutSuccessEvent(auth2Authentication));

		return R.ok();
	}

}
