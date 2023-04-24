package org.ballcat.springsecurity.oauth2.server.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用于序列化 OAuth2Authorization 的专用 ObjectMapper 定制器
 *
 * @author hccake
 */
@FunctionalInterface
public interface OAuth2AuthorizationObjectMapperCustomizer {

	void customize(ObjectMapper objectMapper);

}
