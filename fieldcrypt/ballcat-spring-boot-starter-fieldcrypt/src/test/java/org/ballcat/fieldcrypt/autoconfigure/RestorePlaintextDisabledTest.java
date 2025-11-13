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

package org.ballcat.fieldcrypt.autoconfigure;

import javax.sql.DataSource;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.ballcat.fieldcrypt.autoconfigure.mapper.UserMapper;
import org.ballcat.fieldcrypt.autoconfigure.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * 当 restore-plaintext=false 时，插入调用后入参对象将保持被加密（不恢复）。
 */
@SpringBootTest(classes = TestApplication.class)
@TestPropertySource(properties = { "ballcat.fieldcrypt.enabled=true", "ballcat.fieldcrypt.restore-plaintext=false",
		"ballcat.fieldcrypt.aes-key=6DvlLtb5S7c49usgi8AaWFlsehzH4QUe" })
class RestorePlaintextDisabledTest {

	@Autowired
	UserMapper userMapper;

	@Autowired
	DataSource dataSource;

	@Test
	void insert_keeps_encrypted_when_restore_disabled() {
		User u = new User();
		u.setId(100L);
		u.setName("U100");
		u.setMobile("18810000000");
		u.setEmail("u100@example.com");
		u.setAddress("SZ");

		Assertions.assertTrue(SqlHelper.retBool(this.userMapper.insert(u)));

		// 当关闭恢复时，入参对象会停留在加密后的值（不是明文）
		Assertions.assertNotEquals("18810000000", u.getMobile());
		Assertions.assertNotEquals("u100@example.com", u.getEmail());
	}

}
