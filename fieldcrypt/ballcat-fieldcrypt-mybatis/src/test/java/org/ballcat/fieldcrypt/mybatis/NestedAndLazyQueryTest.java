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
import org.ballcat.fieldcrypt.mybatis.repository.UserJdbcRepository;
import org.ballcat.fieldcrypt.mybatis.testmodel.User;
import org.ballcat.fieldcrypt.mybatis.testmodel.UserDetailView;
import org.ballcat.fieldcrypt.mybatis.testutil.InterceptorTestHarness;
import org.ballcat.fieldcrypt.mybatis.testutil.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 嵌套与延迟查询加/解密验证.
 *
 * @author Hccake
 * @since 2.0.0
 */
@DisplayName("嵌套与延迟查询加/解密验证")
public class NestedAndLazyQueryTest {

	private static final InterceptorTestHarness harness = new InterceptorTestHarness();

	private static final SqlSessionFactory lazyFactory = harness.factory(TestConfig.withLazy(true));

	private static final UserJdbcRepository userRepo = harness.userRepo();

	@BeforeEach
	void clean() {
		harness.truncate();
	}

	private static void insertUser(UserMapper mapper, long id, String name, String mobile, String email,
			String address) {
		User u = new User();
		u.setId(id);
		u.setName(name);
		u.setMobile(mobile);
		u.setEmail(email);
		u.setAddress(address);
		mapper.insert(u);
	}

	@Test
	void testNestedOneSelectDecryptsFields() {
		try (SqlSession session = lazyFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 1L, "N1", "18800000000", "n1@example.com", "ADDR");
			User user = userRepo.findById(1L);

			UserDetailView v = mapper.selectDetailById(1L);
			Assertions.assertEquals(1L, v.getId());
			Assertions.assertEquals("N1", v.getName());
			// 触发延迟加载的嵌套 selectById，并校验返回的嵌套对象字段解密
			User nested = v.getUser();
			Assertions.assertEquals("18800000000", nested.getMobile());
			Assertions.assertEquals("n1@example.com", nested.getEmail());
		}
	}

	@Test
	void testLazyParameterEncryptionOnDemand() {
		try (SqlSession session = lazyFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 2L, "L1", "19900000001", null, null);
			int n = mapper.countByMobileForLazy("19900000001");
			Assertions.assertEquals(1, n);
		}
	}

}
