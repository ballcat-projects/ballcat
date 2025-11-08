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

package org.ballcat.fieldcrypt.mybatis.repository;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.ballcat.fieldcrypt.mybatis.testmodel.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 测试用 JdbcRepository.
 *
 * @author Hccake
 * @since 2.0.0
 */
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

	public List<User> findByIds(Long... ids) {
		if (ids == null || ids.length == 0) {
			return Collections.emptyList();
		}
		StringBuilder sb = new StringBuilder("SELECT id, name, mobile, email, address FROM t_user WHERE id IN (");
		for (int i = 0; i < ids.length; i++) {
			if (i > 0) {
				sb.append(',');
			}
			sb.append('?');
		}
		sb.append(") ORDER BY id");
		return this.jdbc.query(sb.toString(), USER_ROW_MAPPER, (Object[]) ids);
	}

	public List<User> findAll() {
		return this.jdbc.query("SELECT id, name, mobile, email, address FROM t_user ORDER BY id", USER_ROW_MAPPER);
	}

}
