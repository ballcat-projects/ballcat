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

package org.ballcat.fieldcrypt.mybatisplus;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.ballcat.fieldcrypt.core.ClassMetaData;
import org.ballcat.fieldcrypt.core.FieldMetaData;
import org.ballcat.fieldcrypt.core.cache.ClassMetaResolver;
import org.ballcat.fieldcrypt.crypto.CryptoContext;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;
import org.ballcat.fieldcrypt.mybatisplus.mapper.UserMapper;
import org.ballcat.fieldcrypt.mybatisplus.model.User;
import org.ballcat.fieldcrypt.mybatisplus.repository.UserJdbcRepository;
import org.ballcat.fieldcrypt.mybatisplus.weave.MpWrapperEncryptWeaver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(classes = { TestApp.class, TestConfig.class })
@DisplayName("MP 参数加密织入-集成测试")
class MybatisPlusEncryptWeaveTest {

	@Autowired
	private UserMapper userMapper;

	private UserJdbcRepository userJdbcRepository;

	@Autowired
	private CryptoEngine crypto;

	@Autowired
	private ClassMetaResolver resolver;

	@Autowired
	private DataSource dataSource;

	@BeforeEach
	void setup() {
		new MpWrapperEncryptWeaver().install();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
		jdbcTemplate.update("DELETE FROM t_user");
		this.userJdbcRepository = new UserJdbcRepository(this.dataSource);
	}

	// ===== helpers =====
	private void insertUser(long id, String name, String mobile, String email, String address) {
		User u = new User();
		u.setId(id);
		u.setName(name);
		u.setMobile(mobile);
		u.setEmail(email);
		u.setAddress(address);
		Assertions.assertTrue(SqlHelper.retBool(this.userMapper.insert(u)));
	}

	private CryptoContext ctxOf(String fieldName) {
		ClassMetaData meta = this.resolver.resolve(User.class);
		for (FieldMetaData fm : meta.getEncryptedFields()) {
			if (fm.getField().getName().equals(fieldName)) {
				return fm.getContext();
			}
		}
		throw new IllegalStateException("No encrypted field: " + fieldName);
	}

	private void assertDbEncryptedAndDecrypts(long id, String column, String expectedPlain) {
		User raw = this.userJdbcRepository.findById(id);
		String cipher;
		switch (column) {
			case "mobile":
				cipher = raw.getMobile();
				break;
			case "email":
				cipher = raw.getEmail();
				break;
			default:
				throw new IllegalArgumentException("Unsupported column: " + column);
		}
		Assertions.assertNotNull(cipher);
		Assertions.assertNotEquals(expectedPlain, cipher, "DB 中不应存明文");
		String plain = this.crypto.decrypt(cipher, ctxOf(column));
		Assertions.assertEquals(expectedPlain, plain, "密文应可解密回明文");
	}

	@Nested
	@DisplayName("插入密文校验")
	class InsertCipherChecks {

		@Test
		@DisplayName("插入后，JDBC 查询 mobile/email 为密文且可解密")
		void insertIsCipherInDb() {
			insertUser(1L, "U1", "18800000000", "u1@example.com", "SZ");
			assertDbEncryptedAndDecrypts(1L, "mobile", "18800000000");
			assertDbEncryptedAndDecrypts(1L, "email", "u1@example.com");

			User one = MybatisPlusEncryptWeaveTest.this.userMapper.selectById(1L);
			Assertions.assertEquals("18800000000", one.getMobile());
			Assertions.assertEquals("u1@example.com", one.getEmail());
		}

	}

	@Nested
	@DisplayName("条件查询：eq/ne 变体")
	class EqNeVariants {

		@BeforeEach
		void initData() {
			insertUser(1L, "A", "18800000000", "a@example.com", "SZ");
		}

		@Test
		@DisplayName("Lambda eq(带 condition=true) 可用明文命中")
		void lambdaEqConditionTrue() {
			LambdaQueryWrapper<User> w = Wrappers.lambdaQuery(User.class).eq(true, User::getMobile, "18800000000");
			User got = MybatisPlusEncryptWeaveTest.this.userMapper.selectOne(w);
			Assertions.assertNotNull(got);
			Assertions.assertEquals(1L, got.getId());
		}

		@Test
		@DisplayName("Lambda eq(带 condition=false) 不参与条件")
		void lambdaEqConditionFalse() {
			LambdaQueryWrapper<User> w = Wrappers.lambdaQuery(User.class)
				.eq(User::getId, 1L)
				.eq(false, User::getMobile, "18800000000");
			User got = MybatisPlusEncryptWeaveTest.this.userMapper.selectOne(w);
			Assertions.assertNotNull(got);
			Assertions.assertEquals(1L, got.getId());
		}

		@Test
		@DisplayName("字符串列 eq(带 condition=true) 可用明文命中")
		void stringEqConditionTrue() {
			QueryWrapper<User> w = Wrappers.query(User.class).eq(true, "mobile", "18800000000");
			User got = MybatisPlusEncryptWeaveTest.this.userMapper.selectOne(w);
			Assertions.assertNotNull(got);
			Assertions.assertEquals(1L, got.getId());
		}

		@Test
		@DisplayName("Lambda ne(带 condition=true) 不命中")
		void lambdaNeConditionTrue() {
			LambdaQueryWrapper<User> w = Wrappers.lambdaQuery(User.class).ne(true, User::getMobile, "18800000000");
			List<User> list = MybatisPlusEncryptWeaveTest.this.userMapper.selectList(w);
			Assertions.assertTrue(list.isEmpty());
		}

	}

	@Nested
	@DisplayName("in/notIn 变体")
	class InNotInVariants {

		@BeforeEach
		void initData() {
			insertUser(1L, "A", "18800000000", "a@example.com", "SZ");
			insertUser(2L, "B", "18800000001", "b@example.com", "GZ");
		}

		@Test
		@DisplayName("Lambda in(Collection) 可用明文集合命中多条")
		void lambdaInCollection() {
			List<String> mobiles = Arrays.asList("18800000000", "18800000001");
			LambdaQueryWrapper<User> w = Wrappers.lambdaQuery(User.class).in(User::getMobile, mobiles);
			List<User> list = MybatisPlusEncryptWeaveTest.this.userMapper.selectList(w);
			Assertions.assertEquals(2, list.size());
		}

		@Test
		@DisplayName("Lambda in(varargs) 可用明文命中多条")
		void lambdaInVarargs() {
			LambdaQueryWrapper<User> w = Wrappers.lambdaQuery(User.class)
				.in(User::getMobile, "18800000000", "18800000001");
			List<User> list = MybatisPlusEncryptWeaveTest.this.userMapper.selectList(w);
			Assertions.assertEquals(2, list.size());
		}

		@Test
		@DisplayName("字符串列 notIn(Collection) 排除目标")
		void stringNotInCollection() {
			List<String> mobiles = Collections.singletonList("18800000000");
			QueryWrapper<User> w = Wrappers.query(User.class).notIn("mobile", mobiles);
			List<User> list = MybatisPlusEncryptWeaveTest.this.userMapper.selectList(w);
			Assertions.assertEquals(1, list.size());
			Assertions.assertEquals(2L, list.get(0).getId());
		}

		@Test
		@DisplayName("in(Collection) 为空集合且 condition=false 跳过")
		void inEmptyCollectionSkip() {
			List<String> mobiles = Collections.emptyList();
			LambdaQueryWrapper<User> w = Wrappers.lambdaQuery(User.class)
				.eq(User::getId, 1L)
				.in(false, User::getMobile, mobiles);
			List<User> list = MybatisPlusEncryptWeaveTest.this.userMapper.selectList(w);
			Assertions.assertEquals(1, list.size());
		}

	}

	@Nested
	@DisplayName("allEq 变体")
	class AllEqVariants {

		@BeforeEach
		void initData() {
			insertUser(1L, "A", "18800000000", "a@example.com", "SZ");
		}

		@Test
		@DisplayName("allEq(String-Key Map)：仅加密 mobile/email，对 address 不加密")
		void allEqStringKeyMap() {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("mobile", "18800000000");
			map.put("email", "a@example.com");
			map.put("address", "SZ");
			QueryWrapper<User> w = Wrappers.query(User.class).allEq(map);
			User got = MybatisPlusEncryptWeaveTest.this.userMapper.selectOne(w);
			Assertions.assertNotNull(got);
			Assertions.assertEquals(1L, got.getId());
		}

		@Test
		@DisplayName("allEq(..., null2IsNull=true)：null 也参与条件")
		void allEqNull2IsNull() {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("mobile", "18800000000");
			map.put("email", null);
			QueryWrapper<User> w = Wrappers.query(User.class).allEq(map, true);
			List<User> list = MybatisPlusEncryptWeaveTest.this.userMapper.selectList(w);
			// email 为 null 的条件无法命中现有记录（email 有值），应为空
			Assertions.assertTrue(list.isEmpty());
		}

		@Test
		@DisplayName("allEq(BiPredicate)：仅对 mobile 生效，email 被过滤")
		void allEqBiPredicate() {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("mobile", "18800000000");
			map.put("email", "a@example.com");
			QueryWrapper<User> w = Wrappers.query(User.class).allEq((k, v) -> !"email".equals(k), map);
			User got = MybatisPlusEncryptWeaveTest.this.userMapper.selectOne(w);
			Assertions.assertNotNull(got);
			Assertions.assertEquals(1L, got.getId());
		}

		@Test
		@DisplayName("Lambda allEq(Function-Key Map)：按字段加密，命中记录")
		void lambdaAllEqFunctionKeyMap() {
			Map<SFunction<User, ?>, Object> map = new LinkedHashMap<>();
			map.put(User::getMobile, "18800000000");
			map.put(User::getEmail, "a@example.com");
			LambdaQueryWrapper<User> w = Wrappers.lambdaQuery(User.class).allEq(map, false);
			User got = MybatisPlusEncryptWeaveTest.this.userMapper.selectOne(w);
			Assertions.assertNotNull(got);
			Assertions.assertEquals(1L, got.getId());
		}

	}

	@Nested
	@DisplayName("更新 set 加密")
	class UpdateSetVariants {

		@BeforeEach
		void initData() {
			insertUser(1L, "A", "18800000000", "a@example.com", "SZ");
		}

		@Test
		@DisplayName("LambdaUpdateWrapper.set 加密更新 mobile")
		void lambdaUpdateWrapperSetLambda() {
			LambdaUpdateWrapper<User> luw = Wrappers.lambdaUpdate(User.class)
				.eq(User::getId, 1L)
				.set(User::getMobile, "18800000002");
			Assertions.assertTrue(SqlHelper.retBool(MybatisPlusEncryptWeaveTest.this.userMapper.update(null, luw)));

			assertDbEncryptedAndDecrypts(1L, "mobile", "18800000002");
			User one = MybatisPlusEncryptWeaveTest.this.userMapper.selectById(1L);
			Assertions.assertEquals("18800000002", one.getMobile());
		}

	}

	@Nested
	@DisplayName("链式多条件去重加密")
	class DedupEncrypt {

		@BeforeEach
		void initData() {
			insertUser(1L, "A", "18800000000", "a@example.com", "SZ");
		}

		@Test
		@DisplayName("同字段多次出现，查询可正常命中")
		void sameFieldMultipleConditions() {
			LambdaQueryWrapper<User> w = Wrappers.lambdaQuery(User.class)
				.eq(User::getMobile, "18800000000")
				.ne(User::getMobile, "not-match");
			User got = MybatisPlusEncryptWeaveTest.this.userMapper.selectOne(w);
			Assertions.assertNotNull(got);
			Assertions.assertEquals(1L, got.getId());
		}

	}

}
