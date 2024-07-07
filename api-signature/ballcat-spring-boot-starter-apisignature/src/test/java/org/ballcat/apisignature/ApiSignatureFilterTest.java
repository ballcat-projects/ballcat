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

package org.ballcat.apisignature;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiSignatureFilterTest {

	@Test
	void testDoFilterInternal() throws Exception {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String accessKey = "valid_access_key";
		String nonce = RandomStringUtils.randomAlphanumeric(32);
		String secretKey = "123456";

		String httpMethod = "POST";
		String requestURI = "/api/v1/test";
		String queryString = "a=1&b=2";
		String requestPayload = "{name: '张三'}";

		String signature = new SignatureBuilder().setHttpMethod(httpMethod)
			.setRequestURI(requestURI)
			.setQueryString(queryString)
			.setRequestPayload(requestPayload)
			.setTimestamp(timestamp)
			.setNonce(nonce)
			.setAccessKey(accessKey)
			.setSecretKey(secretKey)
			.build();
		ApiSignatureProperties apiSignatureProperties = new ApiSignatureProperties();

		// 创建一个 NonceStore 的模拟对象
		NonceStore mockNonceStore = Mockito.mock(NonceStore.class);
		// 定义当调用 mockNonceStore 的某个方法时，应该返回什么结果
		long nonceTimeout = apiSignatureProperties.getNonceTimeout();
		TimeUnit nonceTimeoutUnit = apiSignatureProperties.getNonceTimeoutUnit();
		Mockito.when(mockNonceStore.storeIfAbsent(nonce, nonceTimeout, nonceTimeoutUnit)).thenReturn(Boolean.TRUE);

		// 创建一个 ApiKeyManager 的模拟对象
		ApiKeyManager mockApiKeyManager = Mockito.mock(ApiKeyManager.class);
		Object subject = new Object();
		Mockito.when(mockApiKeyManager.getSubject(accessKey)).thenReturn(subject);
		Mockito.when(mockApiKeyManager.getSecretKey(subject)).thenReturn(secretKey);

		// 创建一个 ApiSignatureFilter 实例
		ApiSignatureFilter filter = new ApiSignatureFilter(apiSignatureProperties, mockApiKeyManager, mockNonceStore);

		// 创建一个模拟的 HttpServletRequest
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod(httpMethod);
		request.setRequestURI(requestURI);
		request.setQueryString(queryString);
		request.setContent(requestPayload.getBytes(StandardCharsets.UTF_8));
		request.addHeader("X-Timestamp", timestamp);
		request.addHeader("X-Nonce", nonce);
		request.addHeader("X-Access-Key", accessKey);
		request.addHeader("X-Signature", signature);

		// 创建一个模拟的 HttpServletResponse
		MockHttpServletResponse response = new MockHttpServletResponse();

		// 调用 doFilterInternal 方法
		filter.doFilterInternal(request, response, (servletRequest, servletResponse) -> {
		});

		// 验证 HttpServletResponse 的状态码和消息
		assertEquals(200, response.getStatus());
	}

	@Test
	void testExclude() throws ServletException, IOException {
		ApiSignatureProperties apiSignatureProperties = new ApiSignatureProperties();
		apiSignatureProperties.setExcludeUrlPattens(Collections.singletonList("/abc/**"));

		// 创建一个 NonceStore 的模拟对象
		NonceStore mockNonceStore = Mockito.mock(NonceStore.class);

		// 创建一个 ApiKeyManager 的模拟对象
		ApiKeyManager mockApiKeyManager = Mockito.mock(ApiKeyManager.class);

		// 创建一个 ApiSignatureFilter 实例
		ApiSignatureFilter filter = new ApiSignatureFilter(apiSignatureProperties, mockApiKeyManager, mockNonceStore);

		FilterChain filterChain = (servletRequest, servletResponse) -> {
		};

		// 创建一个模拟的 HttpServletRequest
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/abc/123");
		MockHttpServletResponse response = new MockHttpServletResponse();
		filter.doFilterInternal(request, response, filterChain);
		assertEquals(200, response.getStatus());

		// 创建一个模拟的 HttpServletRequest
		MockHttpServletRequest request2 = new MockHttpServletRequest();
		request2.setRequestURI("/cba/123");
		MockHttpServletResponse response2 = new MockHttpServletResponse();
		filter.doFilterInternal(request2, response2, filterChain);
		assertEquals(401, response2.getStatus());
	}

}
