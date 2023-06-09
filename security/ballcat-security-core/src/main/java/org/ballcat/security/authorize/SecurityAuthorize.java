package org.ballcat.security.authorize;

import org.ballcat.common.util.ArrayUtils;
import org.ballcat.security.resources.SecurityHolder;
import org.ballcat.security.resources.SecurityScope;
import org.ballcat.security.annotation.Authorize;
import org.ballcat.security.exception.AuthorizationException;
import org.ballcat.security.exception.PermissionsException;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author lingting 2023-03-29 20:45
 */
@RequiredArgsConstructor
public class SecurityAuthorize {

	/**
	 * 校验当前权限数据是否满足指定注解的要求
	 */
	public void valid(Authorize authorize) throws PermissionsException {
		// 未配置, 要求登录
		if (authorize == null) {
			validLogin();
			return;
		}

		// 允许匿名, 直接执行
		if (authorize.anyone()) {
			return;
		}

		// 非匿名
		validLogin();

		// 要求系统
		if (authorize.onlySystem()) {
			valid(SecurityScope::isSystem);
		}

		// 要求普通用户
		if (authorize.onlyNormal()) {
			valid(scope -> !scope.isSystem());
		}

		// 校验拥有配置
		validHas(authorize);

		// 校验为拥有配置
		validNot(authorize);
	}

	protected void validHas(Authorize authorize) {
		// 要求所有角色
		valid(scope -> equals(scope.getRoles(), authorize.hasRole()));
		// 要求任一角色
		valid(scope -> contains(scope.getRoles(), authorize.hasAnyRole()));
		// 要求所有权限
		valid(scope -> equals(scope.getPermissions(), authorize.hasPermissions()));
		// 要求任一权限
		valid(scope -> contains(scope.getPermissions(), authorize.hasAnyPermissions()));
	}

	protected void validNot(Authorize authorize) {
		// @formatter:off
		// 要求未拥有所有角色
		valid(scope ->  ArrayUtils.isEmpty(authorize.notRole()) || !equals(scope.getRoles(), authorize.notRole()));
		// 要求未拥有任一角色
		valid(scope ->  ArrayUtils.isEmpty(authorize.notAnyRole()) || !contains(scope.getRoles(), authorize.notAnyRole()));
		// 要求未拥有所有权限
		valid(scope ->  ArrayUtils.isEmpty(authorize.notPermissions()) || !equals(scope.getPermissions(), authorize.notPermissions()));
		// 要求未拥有任一权限
		valid(scope ->  ArrayUtils.isEmpty(authorize.notAnyPermissions()) || !contains(scope.getPermissions(), authorize.notAnyPermissions()));
		// @formatter:on
	}

	protected void validLogin() {
		valid(scope -> {
			if (scope == null || !scope.isLogin()) {
				// 未登录让前端登录
				throw new AuthorizationException();
			}
			// 权限不足
			return scope.enabled();
		});
	}

	protected boolean contains(Collection<String> havaArray, String[] needArray) {
		// 需要为空. true
		if (ArrayUtils.isEmpty(needArray)) {
			return true;
		}
		// 拥有为空 false
		if (CollectionUtils.isEmpty(havaArray)) {
			return false;
		}

		for (String need : needArray) {
			// 任一存在则true
			if (havaArray.contains(need)) {
				return true;
			}
		}
		return false;
	}

	protected boolean equals(Collection<String> havaArray, String[] needArray) {
		// 需要为空. true
		if (ArrayUtils.isEmpty(needArray)) {
			return true;
		}
		// 拥有为空 false
		if (CollectionUtils.isEmpty(havaArray)) {
			return false;
		}

		for (String need : needArray) {
			// 任一不存在则false
			if (!havaArray.contains(need)) {
				return false;
			}
		}
		return true;
	}

	protected void valid(Predicate<SecurityScope> predicate) {
		SecurityScope scope = SecurityHolder.scope();
		boolean flag = predicate.test(scope);
		if (!flag) {
			throw new PermissionsException();
		}
	}

}
