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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.ballcat.fieldcrypt.autoconfigure.mapper.UserMapper;
import org.ballcat.fieldcrypt.autoconfigure.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
@TestPropertySource(properties = { "ballcat.fieldcrypt.aes-key-password=TkGV7K3F3LAX9YvgM" })
class MybatisPlusStarterIntegrationTest {

	@Autowired
	UserMapper userMapper;

	@Autowired
	DataSource dataSource;

	@Test
	void insertAndQuery_decrypts_via_starter_auto_config() {
		JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		jdbc.update("DELETE FROM t_user");

		User u = new User();
		u.setId(1L);
		u.setName("U1");
		u.setMobile("18800000000");
		u.setEmail("u1@example.com");
		u.setAddress("SZ");
		Assertions.assertTrue(SqlHelper.retBool(this.userMapper.insert(u)));

		// 参数应被恢复为明文
		Assertions.assertEquals("18800000000", u.getMobile());
		Assertions.assertEquals("u1@example.com", u.getEmail());

		// DB 中应为密文
		User raw = jdbc.queryForObject("select * from t_user where id=1", new BeanPropertyRowMapper<>(User.class));
		Assertions.assertNotNull(raw);
		Assertions.assertNotNull(raw.getMobile());
		Assertions.assertNotEquals("18800000000", raw.getMobile());
		Assertions.assertNotNull(raw.getEmail());
		Assertions.assertNotEquals("u1@example.com", raw.getEmail());

		// 通过 MyBatis-Plus 查询应解密为明文
		User one = this.userMapper.selectById(1L);
		Assertions.assertEquals("18800000000", one.getMobile());
		Assertions.assertEquals("u1@example.com", one.getEmail());

		// Wrapper 条件应以明文可命中
		LambdaQueryWrapper<User> w = Wrappers.lambdaQuery(User.class).eq(User::getMobile, "18800000000");
		User got = this.userMapper.selectOne(w);
		Assertions.assertNotNull(got);
		Assertions.assertEquals(1, got.getId());
	}

}
