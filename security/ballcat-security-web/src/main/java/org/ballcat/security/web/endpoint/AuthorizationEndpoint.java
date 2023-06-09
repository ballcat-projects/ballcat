package org.ballcat.security.web.endpoint;

import lombok.RequiredArgsConstructor;
import org.ballcat.security.SecurityStore;
import org.ballcat.security.annotation.Authorize;
import org.ballcat.security.authorize.SecurityAuthorizationService;
import org.ballcat.security.exception.AuthorizationException;
import org.ballcat.security.mapstruct.SecurityMapstruct;
import org.ballcat.security.password.SecurityPassword;
import org.ballcat.security.po.AuthorizationPasswordPO;
import org.ballcat.security.resources.SecurityHolder;
import org.ballcat.security.resources.SecurityScope;
import org.ballcat.security.vo.AuthorizationVO;
import org.ballcat.security.web.configuration.SecurityWebAuthorizationConfiguration;
import org.ballcat.security.web.constant.SecurityWebConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lingting 2023-03-30 13:30
 */

@RestController
@RequiredArgsConstructor
@ConditionalOnBean(SecurityWebAuthorizationConfiguration.class)
public class AuthorizationEndpoint {

	private final SecurityAuthorizationService service;

	private final SecurityStore store;

	private final SecurityPassword securityPassword;

	@Authorize
	@DeleteMapping(SecurityWebConstants.URI_LOGOUT)
	public AuthorizationVO logout() {
		SecurityScope scope = SecurityHolder.scope();
		store.deleted(scope);
		return SecurityMapstruct.INSTANCE.toVo(scope);
	}

	@Authorize(anyone = true)
	@GetMapping(SecurityWebConstants.URI_PASSWORD)
	public AuthorizationVO password(AuthorizationPasswordPO po) {
		String username = po.getUsername();
		String rawPassword = po.getPassword();
		String password = securityPassword.decodeFront(rawPassword);
		SecurityScope scope = service.validAndBuildScope(username, password);
		if (scope == null) {
			throw new AuthorizationException("用户名或者密码错误!");
		}
		store.save(scope);
		return SecurityMapstruct.INSTANCE.toVo(scope);
	}

	@Authorize
	@GetMapping(SecurityWebConstants.URI_REFRESH)
	public AuthorizationVO refresh() {
		SecurityScope scope = service.refresh(SecurityHolder.token());
		if (scope == null) {
			throw new AuthorizationException("登录授权已失效!");
		}
		store.update(scope);
		return SecurityMapstruct.INSTANCE.toVo(scope);
	}

	@Authorize
	@GetMapping(SecurityWebConstants.URI_RESOLVE)
	public AuthorizationVO resolve() {
		SecurityScope scope = SecurityHolder.scope();
		return SecurityMapstruct.INSTANCE.toVo(scope);
	}

}
