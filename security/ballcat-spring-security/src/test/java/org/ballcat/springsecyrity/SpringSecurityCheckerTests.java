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

package org.ballcat.springsecyrity;

import org.ballcat.security.access.expression.DefaultSecurityExpressionHandler;
import org.ballcat.security.authorization.AuthorizeInterceptor;
import org.ballcat.security.authorization.DefaultAuthorizeManager;
import org.ballcat.security.exception.AccessDeniedException;
import org.ballcat.springsecurity.authorization.SpringSecurityChecker;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpringSecurityCheckerTests {

	@Test
	public void test() {

		Authentication authentication = new TestingAuthenticationToken("user", "123456", "book:read", "book:edit",
				"ROLE_admin");
		SecurityContextImpl securityContext = new SecurityContextImpl(authentication);

		// 创建一个 ProxyFactory 对象
		ProxyFactory proxyFactory = new ProxyFactory();

		// 设置要代理的接口和拦截器
		proxyFactory.setInterfaces(BookService.class);
		proxyFactory.setTarget(new BookServiceImpl());
		SpringSecurityChecker securityChecker = new SpringSecurityChecker();
		DefaultSecurityExpressionHandler expressionHandler = new DefaultSecurityExpressionHandler(securityChecker);
		DefaultAuthorizeManager authorizeManager = new DefaultAuthorizeManager(expressionHandler);
		proxyFactory.addAdvice(new AuthorizeInterceptor(authorizeManager));

		// 创建代理对象
		BookService bookService = (BookService) proxyFactory.getProxy();

		try {
			SecurityContextHolder.setContext(securityContext);

			// 可读
			String bookName = "十万个为什么";
			assertEquals(bookName + " read", bookService.readBook(bookName));

			// 读取格林童话时，传错误的参数，无权限
			assertThrows(AccessDeniedException.class, () -> bookService.readSpecified(bookName));

			// 可改
			assertEquals(bookName + " edited", bookService.editBook(bookName));

			// 无权限编辑
			assertThrows(AccessDeniedException.class, () -> bookService.removeBook(bookName));
		}
		finally {
			SecurityContextHolder.clearContext();
		}

	}

}
