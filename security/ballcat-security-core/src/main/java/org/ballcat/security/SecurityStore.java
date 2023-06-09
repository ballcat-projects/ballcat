package org.ballcat.security;

import org.ballcat.security.resources.SecurityScope;

/**
 * @author lingting 2023-04-06 15:55
 */
public interface SecurityStore {

	void save(SecurityScope scope);

	void update(SecurityScope scope);

	void deleted(SecurityScope scope);

	SecurityScope get(String token);

}
