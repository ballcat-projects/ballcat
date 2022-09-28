package com.hccake.ballcat.common.security.jackson2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.hccake.ballcat.common.security.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * 自定义的 User jackson 反序列化器
 * <p>
 * 参考 {@link org.springframework.security.jackson2.UserDeserializer}
 *
 * @author hccake
 */
public class UserDeserializer extends JsonDeserializer<User> {

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
	public User deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		JsonNode jsonNode = mapper.readTree(jp);

		JsonNode passwordNode = readJsonNode(jsonNode, "password");
		int userId = readJsonNode(jsonNode, "userId").asInt();
		String username = readJsonNode(jsonNode, "username").asText();
		String nickname = readJsonNode(jsonNode, "nickname").asText();
		String avatar = readJsonNode(jsonNode, "avatar").asText();
		int status = readJsonNode(jsonNode, "status").asInt();
		int organizationId = readJsonNode(jsonNode, "organizationId").asInt();
		int type = readJsonNode(jsonNode, "type").asInt();

		String password = passwordNode.asText("");
		if (passwordNode.asText(null) == null) {
			password = null;
		}

		Collection<? extends GrantedAuthority> authorities = mapper.convertValue(jsonNode.get("authorities"),
				SIMPLE_GRANTED_AUTHORITY_SET);

		Map<String, Object> attributes = mapper.convertValue(jsonNode.get("attributes"), ATTRIBUTE_MAP);

		return new User(userId, username, password, nickname, avatar, status, organizationId, type, authorities,
				attributes);
	}

	private JsonNode readJsonNode(JsonNode jsonNode, String field) {
		return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
	}

}
