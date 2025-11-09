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

package org.ballcat.fieldcrypt.mybatisplus.repository;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.ballcat.fieldcrypt.mybatisplus.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserJdbcRepository {

	private final JdbcTemplate jdbc;

	public UserJdbcRepository(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}

	private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> {
		User u = new User();
		u.setId(rs.getLong("id"));
		u.setName(rs.getString("name"));
		u.setMobile(rs.getString("mobile"));
		try {
			u.setEmail(rs.getString("email"));
		}
		catch (SQLException ignore) {
		}
		try {
			u.setAddress(rs.getString("address"));
		}
		catch (SQLException ignore) {
		}
		return u;
	};

	public User findById(long id) {
		return this.jdbc.queryForObject("SELECT id, name, mobile, email, address FROM t_user WHERE id=?",
				USER_ROW_MAPPER, id);
	}

}
