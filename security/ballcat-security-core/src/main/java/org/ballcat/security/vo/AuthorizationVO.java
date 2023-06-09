package org.ballcat.security.vo;

import lombok.Data;

import java.util.Set;

/**
 * @author lingting 2023-03-30 13:54
 */
@Data
public class AuthorizationVO {

	private String token;

	private String userId;

	private String tenantId;

	private String username;

	private Boolean isSystem;

	/**
	 * 是否启用
	 */
	private Boolean isEnabled;

	private Set<String> roles;

	private Set<String> permissions;

}
