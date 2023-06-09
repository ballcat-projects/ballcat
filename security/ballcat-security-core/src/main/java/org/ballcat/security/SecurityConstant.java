package org.ballcat.security;

import lombok.experimental.UtilityClass;

/**
 * @author lingting 2023-03-29 20:33
 */
@UtilityClass
public class SecurityConstant {

	/**
	 * 资源服务器鉴权优先级.
	 */
	public static final int ORDER_RESOURCE_ASPECTJ = 110;

	/**
	 * 资源服务设置 SecurityScope 优先级.
	 */
	public static final int ORDER_RESOURCE_SCOPE = 110;

}
