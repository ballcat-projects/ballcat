/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.springsecurity.oauth2.jackson2;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.ballcat.springsecurity.oauth2.userdetails.DefaultOAuth2User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * 自定义的 User jackson 反序列化器
 * <p>
 * 参考 {@link org.springframework.security.jackson2.UserDeserializer}
 *
 * @author hccake
 */
public class UserDeserializer extends JsonDeserializer<DefaultOAuth2User> {

	private static final TypeReference<Collection<SimpleGrantedAuthority>> SIMPLE_GRANTED_AUTHORITY_SET = new TypeReference<Collection<SimpleGrantedAuthority>>() {
	};

	private static final TypeReference<Map<String, Object>> ATTRIBUTE_MAP = new TypeReference<Map<String, Object>>() {
	};

	/**
	 * This method will create {@link org.springframework.security.core.userdetails.User}
	 * object. It will ensure successful object creation even if password key is null in
	 * serialized json, because credentials may be removed from the
	 * {@link org.springframework.security.core.userdetails.User} by invoking
	 * {@link org.springframework.security.core.userdetails.User#eraseCredentials()}. In
	 * that case there won't be any password key in serialized json.
	 * @param jp the JsonParser
	 * @param ctxt the DeserializationContext
	 * @return the user
	 * @throws IOException if a exception during IO occurs
	 * @throws JsonProcessingException if an error during JSON processing occurs
	 */
	@Override
	public DefaultOAuth2User deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		JsonNode jsonNode = mapper.readTree(jp);

		JsonNode passwordNode = readJsonNode(jsonNode, "password");
		long userId = readJsonNode(jsonNode, "userId").asLong();
		String username = readJsonNode(jsonNode, "username").asText("");
		String nickname = readJsonNode(jsonNode, "nickname").asText("");
		String avatar = readJsonNode(jsonNode, "avatar").asText("");
		int status = readJsonNode(jsonNode, "status").asInt();
		long organizationId = readJsonNode(jsonNode, "organizationId").asLong();
		int type = readJsonNode(jsonNode, "type").asInt();
		String email = readJsonNode(jsonNode, "email").asText("");
		String phoneNumber = readJsonNode(jsonNode, "phoneNumber").asText("");
		int gender = readJsonNode(jsonNode, "gender").asInt();

		String password = passwordNode.asText("");
		if (passwordNode.asText(null) == null) {
			password = null;
		}

		Collection<? extends GrantedAuthority> authorities = mapper.convertValue(jsonNode.get("authorities"),
				SIMPLE_GRANTED_AUTHORITY_SET);

		Map<String, Object> attributes = mapper.convertValue(jsonNode.get("attributes"), ATTRIBUTE_MAP);

		return DefaultOAuth2User.builder()
			.userId(userId)
			.username(username)
			.password(password)
			.nickname(nickname)
			.avatar(avatar)
			.status(status)
			.organizationId(organizationId)
			.email(email)
			.phoneNumber(phoneNumber)
			.gender(gender)
			.type(type)
			.authorities(authorities)
			.attributes(attributes)
			.build();
	}

	private JsonNode readJsonNode(JsonNode jsonNode, String field) {
		return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
	}

}
