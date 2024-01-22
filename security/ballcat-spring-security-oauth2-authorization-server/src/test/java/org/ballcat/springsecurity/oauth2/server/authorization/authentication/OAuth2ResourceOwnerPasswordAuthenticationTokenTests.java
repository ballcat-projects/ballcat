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

package org.ballcat.springsecurity.oauth2.server.authorization.authentication;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.ballcat.springsecurity.oauth2.server.authorization.client.TestRegisteredClients;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link OAuth2ResourceOwnerPasswordAuthenticationToken}.
 *
 * @author hccake
 */
class OAuth2ResourceOwnerPasswordAuthenticationTokenTests {

	private static final RegisteredClient REGISTERED_CLIENT = TestRegisteredClients.registeredClient().build();

	private static final OAuth2ClientAuthenticationToken CLIENT_PRINCIPAL = new OAuth2ClientAuthenticationToken(
			REGISTERED_CLIENT, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, REGISTERED_CLIENT.getClientId());

	@Test
	void constructorWhenUsernameNotProvidedThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> new OAuth2ResourceOwnerPasswordAuthenticationToken(null, CLIENT_PRINCIPAL, null, null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("username cannot be empty");
	}

	@Test
	void constructorWhenPrincipalNotProvidedThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> new OAuth2ResourceOwnerPasswordAuthenticationToken("user1", null, null, null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("clientPrincipal cannot be null");
	}

	@Test
	void constructorWhenAuthorizationRequestThenValuesAreSet() {
		Set<String> requestedScopes = REGISTERED_CLIENT.getScopes();
		Map<String, Object> additionalParameters = Collections.singletonMap("param1", "value1");
		String username = "user1";

		OAuth2ResourceOwnerPasswordAuthenticationToken authentication = new OAuth2ResourceOwnerPasswordAuthenticationToken(
				username, CLIENT_PRINCIPAL, additionalParameters, requestedScopes);

		assertThat(authentication.getPrincipal()).isEqualTo(CLIENT_PRINCIPAL);
		assertThat(authentication.getCredentials()).isEqualTo("");
		assertThat(authentication.getAuthorities()).isEmpty();
		assertThat(authentication.getName()).isEqualTo(username);
		assertThat(authentication.getScopes()).containsExactlyInAnyOrderElementsOf(requestedScopes);
		assertThat(authentication.getAdditionalParameters()).containsExactlyInAnyOrderEntriesOf(additionalParameters);
		assertThat(authentication.isAuthenticated()).isFalse();
	}

}
