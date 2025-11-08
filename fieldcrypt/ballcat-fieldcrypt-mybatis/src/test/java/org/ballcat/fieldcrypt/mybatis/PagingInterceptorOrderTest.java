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

package org.ballcat.fieldcrypt.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.ballcat.fieldcrypt.mybatis.mapper.UserMapper;
import org.ballcat.fieldcrypt.mybatis.testmodel.User;
import org.ballcat.fieldcrypt.mybatis.testutil.InterceptorTestHarness;
import org.ballcat.fieldcrypt.mybatis.testutil.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("分页风格拦截器顺序下的参数加密验证")
public class PagingInterceptorOrderTest {

	private static final InterceptorTestHarness harness = new InterceptorTestHarness();

	private static final SqlSessionFactory factoryWithPagingLike = harness.factory(TestConfig.withPagingLike(true));

	@BeforeEach
	void clean() {
		harness.truncate();
	}

	private static void insertUser(UserMapper mapper, long id, String name, String mobile) {
		User u = new User();
		u.setId(id);
		u.setName(name);
		u.setMobile(mobile);
		mapper.insert(u);
	}

	@Test
	void testEncryptParameterWhenPagingLikeInterceptorPresent() {
		try (SqlSession session = factoryWithPagingLike.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 1L, "P1", "16600000000");
			int n = mapper.countByMobileSingle("16600000000");
			Assertions.assertEquals(1, n);
		}
	}

}
