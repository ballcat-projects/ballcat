package org.ballcat.springsecurity.oauth2.server.authorization.user;

import com.hccake.ballcat.common.security.constant.UserAttributeNameConstants;
import com.hccake.ballcat.common.security.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hccake
 */
public class TestUsers {

	public static User.UserBuilder user1() {
		List<String> roleCodes = Collections.singletonList("ROLE_admin");
		List<String> permissions = Collections.singletonList("user:read");

		List<SimpleGrantedAuthority> authorities = Stream.of(roleCodes, permissions)
			.flatMap(Collection::stream)
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		// 默认将角色和权限放入属性中
		HashMap<String, Object> attributes = new HashMap<>(8);
		attributes.put(UserAttributeNameConstants.ROLE_CODES, roleCodes);
		attributes.put(UserAttributeNameConstants.PERMISSIONS, permissions);

		return User.builder()
			.userId(1)
			.type(1)
			.username("user1")
			.password("password1")
			.nickname("用户1")
			.status(1)
			.avatar("http://s.com")
			.organizationId(1)
			.authorities(authorities)
			.attributes(attributes);
	}

}
