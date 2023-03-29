package org.ballcat.springsecurity.oauth2.server.resource.introspection;

import cn.hutool.core.collection.CollUtil;
import com.hccake.ballcat.common.security.constant.TokenAttributeNameConstants;
import com.hccake.ballcat.common.security.constant.UserAttributeNameConstants;
import com.hccake.ballcat.common.security.constant.UserInfoFiledNameConstants;
import com.hccake.ballcat.common.security.userdetails.ClientPrincipal;
import com.hccake.ballcat.common.security.userdetails.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * copy from {@link SpringOpaqueTokenIntrospector}，重写了 OAuth2AuthenticatedPrincipal
 * 的构建，保持项目内统一使用 {@link com.hccake.ballcat.common.security.userdetails.User}
 *
 * A Spring implementation of {@link OpaqueTokenIntrospector} that verifies and
 * introspects a token using the configured
 * <a href="https://tools.ietf.org/html/rfc7662" target="_blank">OAuth 2.0 Introspection
 * Endpoint</a>.
 *
 * @author Josh Cummings
 * @author Hccake
 * @since 1.1.0
 */
public class BallcatRemoteOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	private static final String AUTHORITY_PREFIX = "SCOPE_";

	private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP = new ParameterizedTypeReference<Map<String, Object>>() {
	};

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final RestOperations restOperations;

	private Converter<String, RequestEntity<?>> requestEntityConverter;

	private static final List<String> INTROSPECTION_CLAIM_NAMES = Arrays.asList(
			OAuth2TokenIntrospectionClaimNames.ACTIVE, OAuth2TokenIntrospectionClaimNames.USERNAME,
			OAuth2TokenIntrospectionClaimNames.CLIENT_ID, OAuth2TokenIntrospectionClaimNames.SCOPE,
			OAuth2TokenIntrospectionClaimNames.TOKEN_TYPE, OAuth2TokenIntrospectionClaimNames.EXP,
			OAuth2TokenIntrospectionClaimNames.IAT, OAuth2TokenIntrospectionClaimNames.NBF,
			OAuth2TokenIntrospectionClaimNames.SUB, OAuth2TokenIntrospectionClaimNames.AUD,
			OAuth2TokenIntrospectionClaimNames.ISS, OAuth2TokenIntrospectionClaimNames.JTI);

	/**
	 * Creates a {@code OpaqueTokenAuthenticationProvider} with the provided parameters
	 * @param introspectionUri The introspection endpoint uri
	 * @param clientId The client id authorized to introspect
	 * @param clientSecret The client's secret
	 */
	public BallcatRemoteOpaqueTokenIntrospector(String introspectionUri, String clientId, String clientSecret) {
		Assert.notNull(introspectionUri, "introspectionUri cannot be null");
		Assert.notNull(clientId, "clientId cannot be null");
		Assert.notNull(clientSecret, "clientSecret cannot be null");
		this.requestEntityConverter = this.defaultRequestEntityConverter(URI.create(introspectionUri));
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(clientId, clientSecret));
		this.restOperations = restTemplate;
	}

	/**
	 * Creates a {@code OpaqueTokenAuthenticationProvider} with the provided parameters
	 * <p>
	 * The given {@link RestOperations} should perform its own client authentication
	 * against the introspection endpoint.
	 * @param introspectionUri The introspection endpoint uri
	 * @param restOperations The client for performing the introspection request
	 */
	public BallcatRemoteOpaqueTokenIntrospector(String introspectionUri, RestOperations restOperations) {
		Assert.notNull(introspectionUri, "introspectionUri cannot be null");
		Assert.notNull(restOperations, "restOperations cannot be null");
		this.requestEntityConverter = this.defaultRequestEntityConverter(URI.create(introspectionUri));
		this.restOperations = restOperations;
	}

	private Converter<String, RequestEntity<?>> defaultRequestEntityConverter(URI introspectionUri) {
		return token -> {
			HttpHeaders headers = requestHeaders();
			MultiValueMap<String, String> body = requestBody(token);
			return new RequestEntity<>(body, headers, HttpMethod.POST, introspectionUri);
		};
	}

	private HttpHeaders requestHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return headers;
	}

	private MultiValueMap<String, String> requestBody(String token) {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("token", token);
		return body;
	}

	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		RequestEntity<?> requestEntity = this.requestEntityConverter.convert(token);
		if (requestEntity == null) {
			throw new OAuth2IntrospectionException("requestEntityConverter returned a null entity");
		}
		ResponseEntity<Map<String, Object>> responseEntity = makeRequest(requestEntity);
		Map<String, Object> claims = adaptToNimbusResponse(responseEntity);
		return convertClaimsSet(claims);
	}

	/**
	 * Sets the {@link Converter} used for converting the OAuth 2.0 access token to a
	 * {@link RequestEntity} representation of the OAuth 2.0 token introspection request.
	 * @param requestEntityConverter the {@link Converter} used for converting to a
	 * {@link RequestEntity} representation of the token introspection request
	 */
	public void setRequestEntityConverter(Converter<String, RequestEntity<?>> requestEntityConverter) {
		Assert.notNull(requestEntityConverter, "requestEntityConverter cannot be null");
		this.requestEntityConverter = requestEntityConverter;
	}

	private ResponseEntity<Map<String, Object>> makeRequest(RequestEntity<?> requestEntity) {
		try {
			return this.restOperations.exchange(requestEntity, STRING_OBJECT_MAP);
		}
		catch (Exception ex) {
			throw new OAuth2IntrospectionException(ex.getMessage(), ex);
		}
	}

	private Map<String, Object> adaptToNimbusResponse(ResponseEntity<Map<String, Object>> responseEntity) {
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new OAuth2IntrospectionException(
					"Introspection endpoint responded with " + responseEntity.getStatusCode());
		}
		Map<String, Object> claims = responseEntity.getBody();
		// relying solely on the authorization server to validate this token (not checking
		// 'exp', for example)
		if (claims == null) {
			return Collections.emptyMap();
		}

		boolean active = (boolean) claims.compute(OAuth2TokenIntrospectionClaimNames.ACTIVE, (k, v) -> {
			if (v instanceof String) {
				return Boolean.parseBoolean((String) v);
			}
			if (v instanceof Boolean) {
				return v;
			}
			return false;
		});
		if (!active) {
			this.logger.trace("Did not validate token since it is inactive");
			throw new BadOpaqueTokenException("Provided token isn't active");
		}
		return claims;
	}

	private OAuth2AuthenticatedPrincipal convertClaimsSet(Map<String, Object> claims) {
		claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.AUD, (k, v) -> {
			if (v instanceof String) {
				return Collections.singletonList(v);
			}
			return v;
		});
		claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.CLIENT_ID, (k, v) -> v.toString());
		claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.EXP,
				(k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
		claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.IAT,
				(k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
		// RFC-7662 page 7 directs users to RFC-7519 for defining the values of these
		// issuer fields.
		// https://datatracker.ietf.org/doc/html/rfc7662#page-7
		//
		// RFC-7519 page 9 defines issuer fields as being 'case-sensitive' strings
		// containing
		// a 'StringOrURI', which is defined on page 5 as being any string, but strings
		// containing ':'
		// should be treated as valid URIs.
		// https://datatracker.ietf.org/doc/html/rfc7519#section-2
		//
		// It is not defined however as to whether-or-not normalized URIs should be
		// treated as the same literal
		// value. It only defines validation itself, so to avoid potential ambiguity or
		// unwanted side effects that
		// may be awkward to debug, we do not want to manipulate this value. Previous
		// versions of Spring Security
		// would *only* allow valid URLs, which is not what we wish to achieve here.
		claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.ISS, (k, v) -> v.toString());
		claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.NBF,
				(k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.SCOPE, (k, v) -> {
			if (v instanceof String) {
				Collection<String> scopes = Arrays.asList(((String) v).split(" "));
				for (String scope : scopes) {
					authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + scope));
				}
				return scopes;
			}
			return v;
		});

		boolean isClient = (boolean) claims.compute(TokenAttributeNameConstants.IS_CLIENT, (k, v) -> {
			if (v instanceof String) {
				return Boolean.parseBoolean((String) v);
			}
			if (v instanceof Boolean) {
				return v;
			}
			logger.warn("自定端点返回的 {} 属性解析异常, 值为: {},", TokenAttributeNameConstants.IS_CLIENT, v);
			return false;
		});

		return isClient ? buildClient(claims, authorities) : buildUser(claims);
	}

	@SuppressWarnings("unchecked")
	private ClientPrincipal buildClient(Map<String, Object> claims, Collection<GrantedAuthority> authorities) {
		String clientId = (String) claims.get(OAuth2TokenIntrospectionClaimNames.CLIENT_ID);
		Collection<String> scopes = (Collection<String>) claims.getOrDefault(OAuth2TokenIntrospectionClaimNames.SCOPE,
				new ArrayList<>());

		ClientPrincipal clientPrincipal = new ClientPrincipal(clientId, claims, authorities);
		clientPrincipal.setScope(scopes);

		return clientPrincipal;
	}

	/**
	 * 根据返回值信息，反向构建出 User 对象
	 * @param claims claims
	 * @return User
	 */
	@SuppressWarnings("unchecked")
	private User buildUser(Map<String, Object> claims) {
		User.UserBuilder builder = User.builder();

		LinkedHashMap<String, Object> info = (LinkedHashMap<String, Object>) claims
			.getOrDefault(TokenAttributeNameConstants.INFO, new LinkedHashMap<>());

		Integer userId = (Integer) info.getOrDefault(UserInfoFiledNameConstants.USER_ID, null);
		if (userId != null) {
			builder.userId(userId);
		}

		Integer type = (Integer) info.getOrDefault(UserInfoFiledNameConstants.TYPE, null);
		if (type != null) {
			builder.type(type);
		}

		Integer organizationId = (Integer) info.getOrDefault(UserInfoFiledNameConstants.ORGANIZATION_ID, null);
		if (organizationId != null) {
			builder.organizationId(organizationId);
		}

		builder.username(info.getOrDefault(UserInfoFiledNameConstants.USERNAME, "").toString())
			.nickname(info.getOrDefault(UserInfoFiledNameConstants.NICKNAME, "").toString())
			.avatar(info.getOrDefault(UserInfoFiledNameConstants.AVATAR, "").toString())
			.status(1);

		Collection<? extends GrantedAuthority> authorities = null;
		List<String> authoritiesJsonArray = (List<String>) claims.getOrDefault("authorities", new ArrayList<>());
		if (authoritiesJsonArray != null) {
			authorities = AuthorityUtils.createAuthorityList((authoritiesJsonArray).toArray(new String[0]));
			builder.authorities(authorities);
		}

		Map<String, Object> attributesMap = (Map<String, Object>) claims
			.getOrDefault(TokenAttributeNameConstants.ATTRIBUTES, new HashMap<>(0));
		if (!CollectionUtils.isEmpty(attributesMap)) {
			claims.putAll(attributesMap);
			// 暂时做下兼容，SAS 不返回 authorities 信息了
			if (CollUtil.isEmpty(authorities)) {
				Collection<String> roleCodes = (Collection<String>) attributesMap
					.getOrDefault(UserAttributeNameConstants.ROLE_CODES, Collections.emptySet());
				Collection<String> permissions = (Collection<String>) attributesMap
					.getOrDefault(UserAttributeNameConstants.PERMISSIONS, Collections.emptySet());
				authorities = Stream.of(roleCodes, permissions)
					.flatMap(Collection::stream)
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet());
				builder.authorities(authorities);
			}
		}

		// 从 claims 中提取 oauth2 中的必须属性
		for (String claimName : INTROSPECTION_CLAIM_NAMES) {
			attributesMap.put(claimName, claims.get(claimName));
		}

		return builder.attributes(attributesMap).build();
	}

}