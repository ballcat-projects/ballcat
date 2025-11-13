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

import org.ballcat.fieldcrypt.autoconfigure.mapper.UserMapper;
import org.ballcat.fieldcrypt.autoconfigure.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 验证在引入 starter 后，纯 MyBatis(非 MyBatis-Plus) 下也能自动加/解密。
 */
@SpringBootTest(classes = TestApplication.class)
@TestPropertySource(properties = { "ballcat.fieldcrypt.aes-key-password=TkGV7K3F3LAX9YvgM" })
class MybatisStarterIntegrationTest {

	@Autowired
	UserMapper mapper;

	@Autowired
	DataSource dataSource;

	@Test
	void insert_encrypts_and_select_decrypts() {
		JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		jdbc.update("DELETE FROM t_user");

		User u = new User();
		u.setId(1L);
		u.setName("U1");
		u.setMobile("18800000000");
		u.setEmail("u1@example.com");
		u.setAddress("SZ");
		assertThat(this.mapper.insert(u)).isEqualTo(1);

		// 参数应被恢复为明文
		assertThat(u.getMobile()).isEqualTo("18800000000");
		assertThat(u.getEmail()).isEqualTo("u1@example.com");

		// JDBC 直查应为密文
		User raw = jdbc.queryForObject("select * from t_user where id=1", new BeanPropertyRowMapper<>(User.class));
		Assertions.assertNotNull(raw);
		assertThat(raw.getMobile()).isNotEqualTo("18800000000");
		assertThat(raw.getEmail()).isNotEqualTo("u1@example.com");

		// MyBatis 查询应解密
		User one = this.mapper.selectById(1L);
		assertThat(one.getMobile()).isEqualTo("18800000000");
		assertThat(one.getEmail()).isEqualTo("u1@example.com");

		User probe = new User();
		probe.setMobile("18800000000");
		User got = this.mapper.selectByMobile(probe);
		// 查询参数也会被恢复
		assertThat(probe.getMobile()).isEqualTo("18800000000");
		assertThat(got).isNotNull();
		assertThat(got.getId()).isEqualTo(1L);
	}

}
