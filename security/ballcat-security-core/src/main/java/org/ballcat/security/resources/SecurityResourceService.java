package org.ballcat.security.resources;

import org.ballcat.security.SecurityToken;

/**
 * 资源服务用
 *
 * @author lingting 2023-03-29 21:19
 */
public interface SecurityResourceService {

	SecurityScope resolve(SecurityToken token);

	default void setScope(SecurityScope scope) {
		SecurityHolder.SCOPE_THREAD_LOCAL.set(scope);
	}

	default void removeScope() {
		SecurityHolder.SCOPE_THREAD_LOCAL.remove();
	}

}
