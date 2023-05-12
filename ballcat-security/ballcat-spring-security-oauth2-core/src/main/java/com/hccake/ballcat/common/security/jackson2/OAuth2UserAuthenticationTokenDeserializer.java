package com.hccake.ballcat.common.security.jackson2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.common.security.authentication.OAuth2UserAuthenticationToken;
import com.hccake.ballcat.common.security.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collection;

/**
 * 自定义的 OAuth2UserAuthenticationToken jackson 反序列化器
 * <p>
 * 参考 {@link org.springframework.security.jackson2.UserDeserializer}
 *
 * @author hccake
 */
public class OAuth2UserAuthenticationTokenDeserializer extends JsonDeserializer<OAuth2UserAuthenticationToken> {

	private static final TypeReference<Collection<SimpleGrantedAuthority>> SIMPLE_GRANTED_AUTHORITY_SET = new TypeReference<Collection<SimpleGrantedAuthority>>() {
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
	public OAuth2UserAuthenticationToken deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		JsonNode jsonNode = mapper.readTree(jp);

		User principal = mapper.treeToValue(jsonNode.get("principal"), User.class);
		Collection<? extends GrantedAuthority> authorities = mapper.convertValue(jsonNode.get("authorities"),
				SIMPLE_GRANTED_AUTHORITY_SET);

		return new OAuth2UserAuthenticationToken(principal, authorities);
	}

}
