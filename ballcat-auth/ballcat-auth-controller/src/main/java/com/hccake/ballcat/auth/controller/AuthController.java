package com.hccake.ballcat.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequiredArgsConstructor
@Tag(name = "用户认证模块")
public class AuthController {

	private final TokenStore tokenStore;

	private final ApplicationEventPublisher publisher;

	/**
	 * 退出token
	 * @param authHeader Authorization
	 */
	@DeleteMapping("/logout")
	public R<Void> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
		if (StrUtil.isBlank(authHeader)) {
			return R.failed(SystemResultCode.FORBIDDEN, "退出失败，token 为空");
		}

		String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StrUtil.EMPTY).trim();
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
		if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
			return R.failed(SystemResultCode.FORBIDDEN, "退出失败，token 无效");
		}

		OAuth2Authentication auth2Authentication = tokenStore.readAuthentication(accessToken);

		// 清空access token
		tokenStore.removeAccessToken(accessToken);
		// 清空 refresh token
		OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
		tokenStore.removeRefreshToken(refreshToken);

		// 发布用户登出事件
		publisher.publishEvent(new LogoutSuccessEvent(auth2Authentication));

		return R.ok();
	}

	// public void test(){
	// // 获取 token 对象
	// String accessToken = "token";
	// OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
	//
	// // 更新 tokenStore 中的数据
	// OAuth2Authentication auth2Authentication =
	// tokenStore.readAuthentication(oAuth2AccessToken);
	// // 新的包含权限的 user 对象
	//
	// User user = (User) auth2Authentication.getDetails();
	// // 更新 user 里的属性
	// UserDetails newUserDetails = new User();
	// auth2Authentication.setDetails(newUserDetails);
	// tokenStore.storeAccessToken(oAuth2AccessToken, auth2Authentication);
	//
	// // 如果后面还有操作需要新权限的话， 更新当前内存中的权限信息
	// SecurityContext context = SecurityContextHolder.getContext();
	// context.setAuthentication(auth2Authentication);
	// SecurityContextHolder.setContext(context);
	//
	// }

}
