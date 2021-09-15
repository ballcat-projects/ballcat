package com.hccake.ballcat.auth;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.common.security.userdetails.ClientPrincipal;
import com.hccake.ballcat.common.security.userdetails.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.Collection;
import java.util.Map;

/**
 * @author hccake
 */
@RequiredArgsConstructor
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		Map<String, Object> response = (Map<String, Object>) super.convertAccessToken(token, authentication);

		// 根据 OAuth2 自省端点协议，scope 应返回字符串，用空格间隔
		// https://datatracker.ietf.org/doc/html/rfc7662
		Object scopeValue = response.get("scope");
		if (scopeValue instanceof Collection) {
			Collection<String> scopes = (Collection<String>) scopeValue;
			response.put("scope", CollectionUtil.join(scopes, " "));
		}

		// 是否是客户端
		boolean isClient = authentication.getPrincipal().getClass().isAssignableFrom(ClientPrincipal.class);
		response.put("is_client", isClient);
		if (isClient) {
			return response;
		}

		// TODO 使用 Scope 进行校验
		// 默认的 CustomTokenEnhancer 在登录获取 token 时只在 attribute 中存放了 ROLE 和 PERMISSION
		// 如果是自己系统内部认可的远程 资源服务器，在拥有权限的情况下，把所有的属性都返回回去
		// 因为实际业务中，可能会在 attributes 中存放一些敏感信息，比如数据权限相关属性
		Collection<? extends GrantedAuthority> requestClientAuthorities = SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();
		if (CollectionUtil.isEmpty(requestClientAuthorities)) {
			return response;
		}

		for (GrantedAuthority authority : requestClientAuthorities) {
			if ("all_attribute".equals(authority.getAuthority())) {
				User principal = (User) authentication.getPrincipal();
				Map<String, Object> attributes = principal.getAttributes();
				response.put("attributes", attributes);
				break;
			}
		}
		return response;
	}

}
