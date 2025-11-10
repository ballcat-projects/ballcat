/*
 * Copyright 2023-2025 the original author or authors.
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

package org.ballcat.fieldcrypt.mybatisplus.encrypt;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for {@link ColumnResolver}.
 */
class ColumnResolverTest {

	@BeforeEach
	void setUp() {
		// Initialize TableInfo for TestUser entity
		TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), TestUser.class);
	}

	@DisplayName("SFunction 解析：默认属性名同列名")
	@Test
	void resolve_sFunction_defaultProperty() {
		ColumnResolver.ColumnRef ref = ColumnResolver.resolve(null, (SFunction<TestUser, ?>) TestUser::getName);
		assertEquals(TestUser.class, ref.getEntityClass());
		assertEquals("name", ref.getPropertyName());
		// default mapping: column equals property name
		assertEquals("name", ref.getColumnName());
	}

	@DisplayName("SFunction 解析：自定义列名映射")
	@Test
	void resolve_sFunction_customColumn() {
		ColumnResolver.ColumnRef ref = ColumnResolver.resolve(null, (SFunction<TestUser, ?>) TestUser::getUserName);
		assertEquals(TestUser.class, ref.getEntityClass());
		assertEquals("userName", ref.getPropertyName());
		// custom column mapping
		assertEquals("user_name", ref.getColumnName());
	}

	@DisplayName("字符串列名解析：主键列命中")
	@Test
	void resolve_stringColumn_primaryKey() {
		QueryWrapper<TestUser> wrapper = new QueryWrapper<>(new TestUser());
		ColumnResolver.ColumnRef ref = ColumnResolver.resolve(wrapper, "id");
		assertEquals(TestUser.class, ref.getEntityClass());
		assertEquals("id", ref.getPropertyName());
		assertEquals("id", ref.getColumnName());
	}

	@DisplayName("字符串列名解析：普通列大小写不敏感")
	@Test
	void resolve_stringColumn_normalColumn_caseInsensitive() {
		QueryWrapper<TestUser> wrapper = new QueryWrapper<>(new TestUser());
		ColumnResolver.ColumnRef ref = ColumnResolver.resolve(wrapper, "USER_NAME");
		assertEquals(TestUser.class, ref.getEntityClass());
		assertEquals("userName", ref.getPropertyName());
		// input keeps as provided column string in ColumnRef
		assertEquals("USER_NAME", ref.getColumnName());
	}

	@DisplayName("不支持的参数类型：返回 null")
	@Test
	void resolve_unsupportedArg_returnsNull() {
		Object unsupported = new Object();
		ColumnResolver.ColumnRef ref = ColumnResolver.resolve(null, unsupported);
		assertNull(ref);
	}

	// simple test entity
	@Setter
	@Getter
	static class TestUser {

		@TableId
		private Long id;

		private String name;

		@TableField("user_name")
		private String userName;

	}

}
