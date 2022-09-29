package org.ballcat.springsecurity.oauth2.server.authorization.authentication;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Map;

/**
 * 在 OAuth2 协议中规定的 Access Token Response 的几种参数之外，添加额外的返回参数
 *
 * @author hccake
 */
public interface AccessTokenResponseEnhancer {

	/**
	 * 生成额外的返回参数 map
	 * @param registeredClient client info
	 * @param principal the current principal
	 * @return map for additional parameters
	 */
	Map<String, Object> enhancer(RegisteredClient registeredClient, Object principal);

}
