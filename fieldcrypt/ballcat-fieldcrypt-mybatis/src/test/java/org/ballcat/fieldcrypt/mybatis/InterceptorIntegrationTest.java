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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.ballcat.fieldcrypt.mybatis.mapper.UserMapper;
import org.ballcat.fieldcrypt.mybatis.repository.UserJdbcRepository;
import org.ballcat.fieldcrypt.mybatis.testmodel.User;
import org.ballcat.fieldcrypt.mybatis.testutil.InterceptorTestHarness;
import org.ballcat.fieldcrypt.mybatis.testutil.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 加/解密拦截器集成测试.
 *
 * @author Hccake
 * @since 2.0.0
 */
@DisplayName("MyBatis 参数加/解密集成测试")
public class InterceptorIntegrationTest {

	private static final InterceptorTestHarness harness = new InterceptorTestHarness();

	private static final SqlSessionFactory sqlSessionFactory = harness.factory(TestConfig.defaults());

	private static final UserJdbcRepository userRepo = harness.userRepo();

	@BeforeEach
	void clean() {
		harness.truncate();
	}

	// ----------------- helpers -----------------
	private static void insertUser(UserMapper mapper, long id, String name, String mobile) {
		User u = new User();
		u.setId(id);
		u.setName(name);
		u.setMobile(mobile);
		mapper.insert(u);
	}

	private static String encryptDeterministic(String plain) {
		return harness.encryptDeterministic(plain);
	}

	// 断言方法移除：在各用例中直接内联明文/密文断言

	private static SqlSessionFactory buildFactory(boolean useActualParamName) {
		return harness.factory(TestConfig.withActualParam(useActualParamName));
	}

	private static SqlSessionFactory buildFactoryWithRestore(boolean restorePlaintext) {
		return harness.factory(TestConfig.withRestore(restorePlaintext));
	}

	private static SqlSessionFactory buildFactoryFailFast(boolean failFast) {
		return harness.factory(TestConfig.withFailFast(failFast));
	}

	@Test
	void testSelectNoParam() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 1L, "NoParam", "18800000000");
			User db = mapper.selectByIdNoParam(1L);
			Assertions.assertAll(() -> Assertions.assertEquals("NoParam", db.getName()),
					() -> Assertions.assertEquals("18800000000", db.getMobile()));
		}
	}

	@Test
	void testInsertAndSelectEncryptDecrypt() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 1L, "Alice", "13800001234");
			// 先校验数据库中存储密文（插入后立即验证加密生效）
			User raw1 = userRepo.findById(1L);
			Assertions.assertEquals(encryptDeterministic("13800001234"), raw1.getMobile());
			// 再执行查询，验证自动解密
			User db = mapper.selectById(1L);
			Assertions.assertAll(() -> Assertions.assertEquals("Alice", db.getName()),
					() -> Assertions.assertEquals("13800001234", db.getMobile()));
		}
	}

	@Test
	void testParameterLevelAnnotation() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			mapper.insertRaw(2L, "Bob", "13900009999");
			// 插入后立即校验密文
			User raw2 = userRepo.findById(2L);
			Assertions.assertEquals(encryptDeterministic("13900009999"), raw2.getMobile());
			// 查询验证解密
			User db = mapper.selectById(2L);
			Assertions.assertAll(() -> Assertions.assertEquals("Bob", db.getName()),
					() -> Assertions.assertEquals("13900009999", db.getMobile()));
		}
	}

	@Test
	void testMapParameterEncryption() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			Map<String, Object> data = new HashMap<>();
			data.put("id", 3L);
			data.put("name", "Carol");
			data.put("mobile", "13700008888"); // 只加密 mobile
			mapper.insertByMap(data);
			// 插入后先校验原始落库（name 明文, mobile 密文）
			User raw3 = userRepo.findById(3L);
			Assertions.assertAll(() -> Assertions.assertEquals("Carol", raw3.getName(), "name 不应被加密"),
					() -> Assertions.assertEquals(encryptDeterministic("13700008888"), raw3.getMobile(), "mobile 应加密"));
			// 再查询验证解密
			User db = mapper.selectById(3L);
			Assertions.assertAll(() -> Assertions.assertEquals("Carol", db.getName()),
					() -> Assertions.assertEquals("13700008888", db.getMobile()));
		}
	}

	@Test
	void testListParameterEncryption() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			User u1 = new User();
			u1.setId(10L);
			u1.setName("L1");
			u1.setMobile("13000000001");
			u1.setEmail("l1@example.com");
			u1.setAddress("BJ");
			User u2 = new User();
			u2.setId(11L);
			u2.setName("L2");
			u2.setMobile("13000000002");
			u2.setEmail("l2@example.com");
			u2.setAddress("SH");
			mapper.batchInsertList(Arrays.asList(u1, u2));
			// 插入后立即验证密文（一次批量查询）
			List<User> raws = userRepo.findByIds(10L, 11L);
			Assertions.assertEquals(2, raws.size());
			Assertions.assertAll(
					() -> Assertions.assertEquals(encryptDeterministic("13000000001"), raws.get(0).getMobile()),
					() -> Assertions.assertEquals(encryptDeterministic("l1@example.com"), raws.get(0).getEmail()),
					() -> Assertions.assertEquals("BJ", raws.get(0).getAddress()),
					() -> Assertions.assertEquals(encryptDeterministic("13000000002"), raws.get(1).getMobile()),
					() -> Assertions.assertEquals(encryptDeterministic("l2@example.com"), raws.get(1).getEmail()),
					() -> Assertions.assertEquals("SH", raws.get(1).getAddress()));
			// 再查询验证解密
			User db1 = mapper.selectById(10L);
			User db2 = mapper.selectById(11L);
			Assertions.assertEquals("13000000001", db1.getMobile());
			Assertions.assertEquals("13000000002", db2.getMobile());
			Assertions.assertEquals("l1@example.com", db1.getEmail());
			Assertions.assertEquals("l2@example.com", db2.getEmail());
			Assertions.assertEquals("BJ", db1.getAddress());
			Assertions.assertEquals("SH", db2.getAddress());
		}
	}

	@Test
	void testArrayParameterEncryption() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			User u1 = new User();
			u1.setId(20L);
			u1.setName("A1");
			u1.setMobile("13100000001");
			u1.setEmail("a1@example.com");
			u1.setAddress("GZ");
			User u2 = new User();
			u2.setId(21L);
			u2.setName("A2");
			u2.setMobile("13100000002");
			u2.setEmail("a2@example.com");
			u2.setAddress("SZ");
			mapper.batchInsertArray(new User[] { u1, u2 });
			// 插入后立即验证密文（一次批量查询）
			List<User> raws2 = userRepo.findByIds(20L, 21L);
			Assertions.assertEquals(2, raws2.size());
			Assertions.assertAll(
					() -> Assertions.assertEquals(encryptDeterministic("13100000001"), raws2.get(0).getMobile()),
					() -> Assertions.assertEquals(encryptDeterministic("a1@example.com"), raws2.get(0).getEmail()),
					() -> Assertions.assertEquals("GZ", raws2.get(0).getAddress()),
					() -> Assertions.assertEquals(encryptDeterministic("13100000002"), raws2.get(1).getMobile()),
					() -> Assertions.assertEquals(encryptDeterministic("a2@example.com"), raws2.get(1).getEmail()),
					() -> Assertions.assertEquals("SZ", raws2.get(1).getAddress()));
			// 再查询验证解密
			User db1 = mapper.selectById(20L);
			User db2 = mapper.selectById(21L);
			Assertions.assertEquals("13100000001", db1.getMobile());
			Assertions.assertEquals("13100000002", db2.getMobile());
			Assertions.assertEquals("a1@example.com", db1.getEmail());
			Assertions.assertEquals("a2@example.com", db2.getEmail());
			Assertions.assertEquals("GZ", db1.getAddress());
			Assertions.assertEquals("SZ", db2.getAddress());
		}
	}

	@Test
	void testUpdateAndNoDoubleEncryptWithAliases() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			User u = new User();
			u.setId(30L);
			u.setName("Upd");
			u.setMobile("13200000000");
			mapper.insert(u);
			// 初次插入后检查密文
			User raw30a = userRepo.findById(30L);
			Assertions.assertEquals(encryptDeterministic("13200000000"), raw30a.getMobile());
			// 修改手机号
			u.setMobile("13200000099");
			mapper.updateMobile(u); // MyBatis 会生成 param1/arg0/user 三个别名
			// 更新后立即检查密文（新值加密）
			User raw30b = userRepo.findById(30L);
			Assertions.assertEquals(encryptDeterministic("13200000099"), raw30b.getMobile());
			// 再查询验证解密为最新明文
			User db = mapper.selectById(30L);
			Assertions.assertEquals("13200000099", db.getMobile());
		}
	}

	@Test
	void testDeleteByEncryptedMobile() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			User u = new User();
			u.setId(40L);
			u.setName("Del");
			u.setMobile("13300000000");
			mapper.insert(u);
			// 插入后先验证密文
			User raw40 = userRepo.findById(40L);
			Assertions.assertEquals(encryptDeterministic("13300000000"), raw40.getMobile());
			// 按手机号删除（传入参数会被加密与库中密文匹配）
			int rows = mapper.deleteByMobile("13300000000");
			Assertions.assertEquals(1, rows);
			User db = mapper.selectById(40L);
			Assertions.assertNull(db); // 已删除
		}
	}

	@Test
	void testSingleStringParameterEncryption() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 100L, "S1", "18000000000");
			// 验证单个 String 根参数被加密匹配
			int n = mapper.countByMobileSingle("18000000000");
			Assertions.assertEquals(1, n);
		}
	}

	@Test
	void testListAndArrayStringParameterEncryption() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 101L, "S2", "18100000001");
			insertUser(mapper, 102L, "S3", "18100000002");
			int n1 = mapper.countByMobiles(Arrays.asList("18100000001", "18100000002"));
			Assertions.assertEquals(2, n1);
			int n2 = mapper.countByMobilesArray(new String[] { "18100000001", "18100000002" });
			Assertions.assertEquals(2, n2);

			int n3 = mapper.countByMobilesWithoutEncrypt(Arrays.asList("18100000001", "18100000002"));
			Assertions.assertEquals(0, n3);
		}
	}

	@Test
	void testSetStringParameterLimitationWithoutEncrypted() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 207L, "ST", "19900000008");

			User raw = userRepo.findById(207L);
			Assertions.assertEquals(encryptDeterministic("19900000008"), raw.getMobile());

			Set<String> set = new HashSet<>();
			set.add("19900000008");
			int n = mapper.countByMobilesSetWithoutEncrypted(set);
			// 当前实现不替换 Set 元素，SQL 收到明文，匹配失败
			Assertions.assertEquals(0, n);
		}
	}

	@Test
	void testMapKeysEncryptionOnQuery() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 103L, "M1", "18200000000");
			Map<String, Object> map = new HashMap<>();
			map.put("mobile", "18200000000");
			int n = mapper.countByMap(map);
			Assertions.assertEquals(1, n);
		}
	}

	@Test
	void testRestorePlaintextForContainers() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(true);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 104L, "R1", "18300000001");
			insertUser(mapper, 105L, "R2", "18300000002");

			List<String> list = new ArrayList<>();
			list.add("18300000001");
			list.add("18300000002");
			String[] arr = new String[] { "18300000001", "18300000002" };
			Map<String, Object> map = new HashMap<>();
			map.put("mobile", "18300000001");

			int c1 = mapper.countByMobiles(list);
			int c2 = mapper.countByMobilesArray(arr);
			int c3 = mapper.countByMap(map);
			Assertions.assertAll(() -> Assertions.assertEquals(2, c1), () -> Assertions.assertEquals(2, c2),
					() -> Assertions.assertEquals(1, c3));
			// 验证容器内元素已恢复为明文
			Assertions.assertAll(() -> Assertions.assertEquals("18300000001", list.get(0)),
					() -> Assertions.assertEquals("18300000002", list.get(1)),
					() -> Assertions.assertEquals("18300000001", arr[0]),
					() -> Assertions.assertEquals("18300000002", arr[1]),
					() -> Assertions.assertEquals("18300000001", map.get("mobile")));
		}
	}

	@Test
	void testSelectByMobileNumericIndex() throws Exception {
		// 此用例单独构建一个关闭 useActualParamName 的 Configuration，以生成数字键“0”“1”。
		SqlSessionFactory localFactory = buildFactory(false);
		try (SqlSession session = localFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			// 插入记录
			insertUser(mapper, 50L, "Idx", "15500000000");
			// 使用 #{1} 查询第二个参数 (mobile)
			User db = mapper.selectByMobileNumericIndex(999L, "15500000000");
			Assertions.assertAll(() -> Assertions.assertEquals("Idx", db.getName()),
					() -> Assertions.assertEquals("15500000000", db.getMobile()));
		}
	}

	@Test
	void testSelectByMobileWithRowBounds() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			// 插入一条记录
			insertUser(mapper, 60L, "RB", "15600000000");
			// 直接校验库中为密文
			User raw60 = userRepo.findById(60L);
			Assertions.assertEquals(encryptDeterministic("15600000000"), raw60.getMobile());
			RowBounds rb = new RowBounds(0, 10);
			User db = mapper.selectByMobileWithRowBounds(rb, "15600000000");
			Assertions.assertAll(() -> Assertions.assertEquals("RB", db.getName()),
					() -> Assertions.assertEquals("15600000000", db.getMobile()));
		}
	}

	@Test
	void testMultiFieldEntityEncryption() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			User u = new User();
			u.setId(70L);
			u.setName("MF");
			u.setMobile("16600000000");
			u.setEmail("mf@example.com");
			u.setAddress("SZ");
			mapper.insert(u);
			// 原始库值检查：mobile/email 加密，name/address 明文（一次查询）
			User raw = userRepo.findById(70L);
			Assertions.assertAll(() -> Assertions.assertEquals(encryptDeterministic("16600000000"), raw.getMobile()),
					() -> Assertions.assertEquals(encryptDeterministic("mf@example.com"), raw.getEmail()),
					() -> Assertions.assertEquals("MF", raw.getName()),
					() -> Assertions.assertEquals("SZ", raw.getAddress()));

			// 查询解密检查
			User db = mapper.selectById(70L);
			Assertions.assertAll(() -> Assertions.assertEquals("MF", db.getName()),
					() -> Assertions.assertEquals("16600000000", db.getMobile()),
					() -> Assertions.assertEquals("mf@example.com", db.getEmail()),
					() -> Assertions.assertEquals("SZ", db.getAddress()));
		}
	}

	@Test
	void testInsertByBatchAndSelectAll() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);

			User u1 = new User();
			u1.setId(90L);
			u1.setName("BL1");
			u1.setMobile("17200000001");
			u1.setEmail("bl1@example.com");
			u1.setAddress("XA");

			User u2 = new User();
			u2.setId(91L);
			u2.setName("BL2");
			u2.setMobile("17200000002");
			u2.setEmail("bl2@example.com");
			u2.setAddress("NJ");

			int n = mapper.insertByBatch(Arrays.asList(u1, u2));
			Assertions.assertEquals(2, n);

			// 校验落库密文（查全部原始数据）
			List<User> raws = userRepo.findAll();
			// 只断言最后两条（按 id 排序，且表在 @BeforeEach 已清空）
			Assertions.assertEquals(2, raws.size());
			Assertions.assertAll(() -> Assertions.assertEquals(90L, raws.get(0).getId()),
					() -> Assertions.assertEquals(encryptDeterministic("17200000001"), raws.get(0).getMobile()),
					() -> Assertions.assertEquals(encryptDeterministic("bl1@example.com"), raws.get(0).getEmail()),
					() -> Assertions.assertEquals("BL1", raws.get(0).getName()),
					() -> Assertions.assertEquals("XA", raws.get(0).getAddress()),
					() -> Assertions.assertEquals(91L, raws.get(1).getId()),
					() -> Assertions.assertEquals(encryptDeterministic("17200000002"), raws.get(1).getMobile()),
					() -> Assertions.assertEquals(encryptDeterministic("bl2@example.com"), raws.get(1).getEmail()),
					() -> Assertions.assertEquals("BL2", raws.get(1).getName()),
					() -> Assertions.assertEquals("NJ", raws.get(1).getAddress()));

			// 使用 mapper 的 selectAll 验证自动解密
			List<User> all = mapper.selectAll();
			Assertions.assertEquals(2, all.size());
			Assertions.assertAll(() -> Assertions.assertEquals(90L, all.get(0).getId()),
					() -> Assertions.assertEquals("17200000001", all.get(0).getMobile()),
					() -> Assertions.assertEquals("bl1@example.com", all.get(0).getEmail()),
					() -> Assertions.assertEquals("BL1", all.get(0).getName()),
					() -> Assertions.assertEquals("XA", all.get(0).getAddress()),
					() -> Assertions.assertEquals(91L, all.get(1).getId()),
					() -> Assertions.assertEquals("17200000002", all.get(1).getMobile()),
					() -> Assertions.assertEquals("bl2@example.com", all.get(1).getEmail()),
					() -> Assertions.assertEquals("BL2", all.get(1).getName()),
					() -> Assertions.assertEquals("NJ", all.get(1).getAddress()));
		}
	}

	@Test
	void testMapWildcardInsertAndQuery() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			Map<String, Object> data = new HashMap<>();
			data.put("id", 200L);
			data.put("name", "WILD");
			data.put("mobile", "19900000001");
			mapper.insertByMapWildcard(data);
			// 原始库：mobile 应为密文，name（字符串）不会落表加密因为 SQL 只用到了 name 文本值，测试关注 mobile
			User raw = userRepo.findById(200L);
			Assertions.assertEquals(encryptDeterministic("19900000001"), raw.getMobile());
			// 查询时自动解密
			User db = mapper.selectById(200L);
			Assertions.assertEquals("19900000001", db.getMobile());
		}
	}

	@Test
	void testMapAbsentKeyNoop() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 201L, "AK", "19900000002");
			Map<String, Object> data = new HashMap<>();
			data.put("mobile", "19900000002");
			int n = mapper.countByMapAbsentKey(data);
			// 未命中 key，计数应按明文执行（实际上不会加密，SQL 仍传明文，匹配失败），这里确认为 0
			Assertions.assertEquals(0, n);
		}
	}

	@Test
	void testMapEntityValueWithWildcard() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 202L, "EW", "19900000003");
			Map<String, Object> data = new HashMap<>();
			User probe = new User();
			probe.setMobile("19900000003");
			data.put("user", probe);
			int n = mapper.countByMapEntityWildcard(data);
			Assertions.assertEquals(1, n);
		}
	}

	@Test
	void testNestedMapNotEncrypted() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 203L, "NM", "19900000004");
			Map<String, Object> inner = new HashMap<>();
			inner.put("mobile", "19900000004");
			Map<String, Object> outer = new HashMap<>();
			outer.put("inner", inner);
			int n = mapper.countByNestedMap(outer);
			// 由于不递归处理嵌套 Map，SQL 将收到明文，匹配失败
			Assertions.assertEquals(0, n);
		}
	}

	@Test
	void testRestorePlaintextForEntity() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(true);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 204L, "RE", "19900000005");
			User u = new User();
			u.setMobile("19900000005");
			int n = mapper.countByUser(u);
			Assertions.assertEquals(1, n);
			// 验证回滚：实体字段恢复为明文
			Assertions.assertEquals("19900000005", u.getMobile());
		}
	}

	@Test
	void testFailFastFalseSwallowsUnknownAlgo() throws Exception {
		SqlSessionFactory sf = buildFactoryFailFast(false);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 205L, "FF", "19900000006");
			// 未注册算法 + failFast=false：应吞错并按明文执行（计数 0）
			int n = mapper.countByMobileUnknownAlgo("19900000006");
			Assertions.assertEquals(0, n);
		}
	}

	@Test
	void testFailFastTrueThrowsOnUnknownAlgo() throws Exception {
		SqlSessionFactory sf = buildFactoryFailFast(true);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 206L, "FT", "19900000007");
			Assertions.assertThrows(Exception.class, () -> mapper.countByMobileUnknownAlgo("19900000007"));
		}
	}

	@Test
	void testSetStringParameterLimitation() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 207L, "ST", "19900000008");

			User raw = userRepo.findById(207L);
			Assertions.assertEquals(encryptDeterministic("19900000008"), raw.getMobile());

			Set<String> set = new HashSet<>();
			set.add("19900000008");
			int n = mapper.countByMobilesSet(set);
			// 当前实现不替换 Set 元素，SQL 收到明文，匹配失败
			Assertions.assertEquals(0, n);
		}
	}

	@Test
	void testInsertByGenericCollection() throws Exception {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			Collection<User> coll = new ArrayList<>();
			User u1 = new User();
			u1.setId(208L);
			u1.setName("GC1");
			u1.setMobile("19900000009");
			u1.setEmail("gc1@example.com");
			u1.setAddress("AA");
			User u2 = new User();
			u2.setId(209L);
			u2.setName("GC2");
			u2.setMobile("19900000010");
			u2.setEmail("gc2@example.com");
			u2.setAddress("BB");
			coll.add(u1);
			coll.add(u2);
			int n = mapper.insertByCollection(coll);
			Assertions.assertEquals(2, n);
			List<User> raws = userRepo.findByIds(208L, 209L);
			Assertions.assertEquals(encryptDeterministic("19900000009"), raws.get(0).getMobile());
			Assertions.assertEquals(encryptDeterministic("gc1@example.com"), raws.get(0).getEmail());
			Assertions.assertEquals(encryptDeterministic("19900000010"), raws.get(1).getMobile());
			Assertions.assertEquals(encryptDeterministic("gc2@example.com"), raws.get(1).getEmail());
		}
	}

	@Test
	void testBatchExecutorInsertEncryption() throws Exception {
		// 使用 MyBatis ExecutorType.BATCH，逐条 insert，由批处理执行，验证加/解密在批模式下同样生效
		try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
			UserMapper mapper = session.getMapper(UserMapper.class);

			User u1 = new User();
			u1.setId(80L);
			u1.setName("B1");
			u1.setMobile("17100000001");
			u1.setEmail("b1@example.com");
			u1.setAddress("CD");

			User u2 = new User();
			u2.setId(81L);
			u2.setName("B2");
			u2.setMobile("17100000002");
			u2.setEmail("b2@example.com");
			u2.setAddress("WH");

			mapper.insert(u1);
			mapper.insert(u2);

			// 刷新批处理并提交，使独立的 dataSource 连接能读到数据
			session.flushStatements();
			session.commit();

			// 原始库值检查（一次性批量查询）
			List<User> raws = userRepo.findByIds(80L, 81L);
			Assertions.assertEquals(2, raws.size());
			Assertions.assertAll(
					() -> Assertions.assertEquals(encryptDeterministic("17100000001"), raws.get(0).getMobile()),
					() -> Assertions.assertEquals(encryptDeterministic("b1@example.com"), raws.get(0).getEmail()),
					() -> Assertions.assertEquals("B1", raws.get(0).getName()),
					() -> Assertions.assertEquals("CD", raws.get(0).getAddress()),
					() -> Assertions.assertEquals(encryptDeterministic("17100000002"), raws.get(1).getMobile()),
					() -> Assertions.assertEquals(encryptDeterministic("b2@example.com"), raws.get(1).getEmail()),
					() -> Assertions.assertEquals("B2", raws.get(1).getName()),
					() -> Assertions.assertEquals("WH", raws.get(1).getAddress()));

			// 解密查询检查（同一会话执行查询亦可）
			User db1 = mapper.selectById(80L);
			User db2 = mapper.selectById(81L);
			Assertions.assertAll(() -> Assertions.assertEquals("B1", db1.getName()),
					() -> Assertions.assertEquals("17100000001", db1.getMobile()),
					() -> Assertions.assertEquals("b1@example.com", db1.getEmail()),
					() -> Assertions.assertEquals("CD", db1.getAddress()),
					() -> Assertions.assertEquals("B2", db2.getName()),
					() -> Assertions.assertEquals("17100000002", db2.getMobile()),
					() -> Assertions.assertEquals("b2@example.com", db2.getEmail()),
					() -> Assertions.assertEquals("WH", db2.getAddress()));
		}
	}

	@Test
	void testSelectMobile() {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 1L, "张三", "18800000000");
			String mobile = mapper.selectMobileById(1L);
			Assertions.assertEquals("18800000000", mobile);
		}
	}

	@Test
	void testSelectMobiles() {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 1L, "张三", "18800000000");
			insertUser(mapper, 2L, "李四", "17700000000");

			List<String> mobiles = mapper.selectMobiles();

			Assertions.assertEquals(2, mobiles.size());
			Assertions.assertAll(() -> Assertions.assertEquals("18800000000", mobiles.get(0)),
					() -> Assertions.assertEquals("17700000000", mobiles.get(1)));
		}
	}

	// ============================================================
	// 补充测试：回滚明文功能的完整覆盖
	// ============================================================

	@Test
	@DisplayName("测试单个 String 参数的回滚 - 启用 restorePlaintext")
	void testRestorePlaintextForSingleString() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(true);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 301L, "RS1", "19100000001");

			// 使用变量保存原始明文，验证方法执行后参数被恢复
			String mobile = "19100000001";
			int n = mapper.countByMobileSingle(mobile);

			Assertions.assertEquals(1, n);
			// 验证：单个字符串参数无法被回滚（因为 String 是不可变的，只能验证功能正常）
			// 注意：Java 中 String 是不可变的，所以这里只能验证查询成功
			Assertions.assertEquals("19100000001", mobile);
		}
	}

	@Test
	@DisplayName("测试单个 String 参数查询 - 禁用 restorePlaintext")
	void testNoRestoreForSingleString() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(false);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 302L, "RS2", "19100000002");

			String mobile = "19100000002";
			int n = mapper.countByMobileSingle(mobile);

			Assertions.assertEquals(1, n);
			// 禁用回滚时，String 不可变性使得参数保持原样
			Assertions.assertEquals("19100000002", mobile);
		}
	}

	@Test
	@DisplayName("测试 List 参数的回滚 - 验证加密后 SQL 执行前不回滚")
	void testListEncryptionTimingWithRestore() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(true);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 303L, "RT1", "19100000003");
			insertUser(mapper, 304L, "RT2", "19100000004");

			List<String> list = new ArrayList<>();
			list.add("19100000003");
			list.add("19100000004");

			// 执行查询：参数应该被加密 -> SQL 执行 -> 然后回滚为明文
			int count = mapper.countByMobiles(list);

			// 关键断言：查询成功找到 2 条记录（说明 SQL 执行时参数是密文）
			Assertions.assertEquals(2, count);

			// 关键断言：方法返回后参数已恢复为明文
			Assertions.assertEquals("19100000003", list.get(0));
			Assertions.assertEquals("19100000004", list.get(1));
		}
	}

	@Test
	@DisplayName("测试 List 参数禁用回滚 - 验证参数保持加密后的密文")
	void testListNoRestore() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(false);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 305L, "NR1", "19100000005");

			List<String> list = new ArrayList<>();
			list.add("19100000005");

			int count = mapper.countByMobiles(list);
			Assertions.assertEquals(1, count);

			// 禁用回滚时，List 中的元素应该保持为密文
			String encryptedValue = list.get(0);
			Assertions.assertNotEquals("19100000005", encryptedValue);
			Assertions.assertEquals(encryptDeterministic("19100000005"), encryptedValue);
		}
	}

	@Test
	@DisplayName("测试 Array 参数的回滚")
	void testArrayRestorePlaintext() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(true);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 306L, "AR1", "19100000006");
			insertUser(mapper, 307L, "AR2", "19100000007");

			String[] arr = new String[] { "19100000006", "19100000007" };
			int count = mapper.countByMobilesArray(arr);

			Assertions.assertEquals(2, count);
			// 验证数组元素已恢复为明文
			Assertions.assertEquals("19100000006", arr[0]);
			Assertions.assertEquals("19100000007", arr[1]);
		}
	}

	@Test
	@DisplayName("测试 Array 参数禁用回滚")
	void testArrayNoRestore() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(false);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 308L, "NRA", "19100000008");

			String[] arr = new String[] { "19100000008" };
			int count = mapper.countByMobilesArray(arr);

			Assertions.assertEquals(1, count);
			// 禁用回滚时，数组元素应该保持为密文
			Assertions.assertNotEquals("19100000008", arr[0]);
			Assertions.assertEquals(encryptDeterministic("19100000008"), arr[0]);
		}
	}

	@Test
	@DisplayName("测试 Map 参数的回滚 - mapKeys 指定 key")
	void testMapRestorePlaintextWithSpecificKeys() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(true);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 309L, "MR1", "19100000009");

			Map<String, Object> map = new HashMap<>();
			map.put("mobile", "19100000009");

			int count = mapper.countByMap(map);

			Assertions.assertEquals(1, count);
			// 验证 Map 中的值已恢复为明文
			Assertions.assertEquals("19100000009", map.get("mobile"));
		}
	}

	@Test
	@DisplayName("测试 Map 参数禁用回滚")
	void testMapNoRestore() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(false);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 310L, "NRM", "19100000010");

			Map<String, Object> map = new HashMap<>();
			map.put("mobile", "19100000010");

			int count = mapper.countByMap(map);

			Assertions.assertEquals(1, count);
			// 禁用回滚时，Map 中的值应该保持为密文
			Assertions.assertNotEquals("19100000010", map.get("mobile"));
			Assertions.assertEquals(encryptDeterministic("19100000010"), map.get("mobile"));
		}
	}

	@Test
	@DisplayName("测试实体对象字段的回滚")
	void testEntityFieldRestore() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(true);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 311L, "ER1", "19100000011");

			User user = new User();
			user.setId(311L);
			user.setName("ER1");
			user.setMobile("19100000011");
			user.setEmail("er1@example.com");

			// 执行更新操作，字段会被加密
			mapper.updateMobile(user);

			// 验证：实体字段已恢复为明文
			Assertions.assertEquals("19100000011", user.getMobile());
			Assertions.assertEquals("er1@example.com", user.getEmail());
		}
	}

	@Test
	@DisplayName("测试实体对象字段禁用回滚")
	void testEntityFieldNoRestore() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(false);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 312L, "NRE", "19100000012");

			User user = new User();
			user.setId(312L);
			user.setMobile("19100000012");

			mapper.updateMobile(user);

			// 禁用回滚时，实体字段应该保持为密文
			Assertions.assertNotEquals("19100000012", user.getMobile());
			Assertions.assertEquals(encryptDeterministic("19100000012"), user.getMobile());
		}
	}

	@Test
	@DisplayName("测试 ParamMap 多 key 指向同一对象的回滚")
	void testParamMapMultipleKeysRestore() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(true);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 313L, "PM1", "19100000013");

			// MyBatis 会为同一个参数生成多个 key: param1, arg0, mobile
			int count = mapper.countByMobileSingle("19100000013");

			Assertions.assertEquals(1, count);
			// 注意：由于 String 不可变，这里只能验证查询成功
		}
	}

	@Test
	@DisplayName("测试混合类型参数的回滚 - List 中包含实体对象")
	void testMixedTypeListRestore() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(true);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);

			List<User> users = new ArrayList<>();
			User u1 = new User();
			u1.setId(314L);
			u1.setName("MT1");
			u1.setMobile("19100000014");
			u1.setEmail("mt1@example.com");

			User u2 = new User();
			u2.setId(315L);
			u2.setName("MT2");
			u2.setMobile("19100000015");
			u2.setEmail("mt2@example.com");

			users.add(u1);
			users.add(u2);

			// 批量插入
			mapper.batchInsertList(users);

			// 验证：实体对象的字段已恢复为明文
			Assertions.assertEquals("19100000014", u1.getMobile());
			Assertions.assertEquals("mt1@example.com", u1.getEmail());
			Assertions.assertEquals("19100000015", u2.getMobile());
			Assertions.assertEquals("mt2@example.com", u2.getEmail());

			// 验证数据库中是密文
			User raw1 = userRepo.findById(314L);
			User raw2 = userRepo.findById(315L);
			Assertions.assertEquals(encryptDeterministic("19100000014"), raw1.getMobile());
			Assertions.assertEquals(encryptDeterministic("mt1@example.com"), raw1.getEmail());
			Assertions.assertEquals(encryptDeterministic("19100000015"), raw2.getMobile());
			Assertions.assertEquals(encryptDeterministic("mt2@example.com"), raw2.getEmail());
		}
	}

	@Test
	@DisplayName("测试 SQL 执行成功后才回滚 - 验证回滚时机")
	void testRestoreOnlyAfterSqlExecution() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(true);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 316L, "TM1", "19100000016");

			List<String> list = new ArrayList<>();
			list.add("19100000016");

			// 执行查询 - 关键：SQL 必须在回滚之前执行
			int count = mapper.countByMobiles(list);

			// 如果回滚发生在 SQL 执行之前，这里会返回 0（因为明文无法匹配密文）
			// 如果回滚发生在 SQL 执行之后，这里会返回 1（正确行为）
			Assertions.assertEquals(1, count, "SQL 执行时参数应该是密文，执行后才回滚为明文");

			// 验证回滚成功
			Assertions.assertEquals("19100000016", list.get(0));
		}
	}

	@Test
	@DisplayName("测试嵌套调用的回滚 - 确保每层调用都正确回滚")
	void testNestedCallsRestore() throws Exception {
		SqlSessionFactory sf = buildFactoryWithRestore(true);
		try (SqlSession session = sf.openSession(true)) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			insertUser(mapper, 317L, "NC1", "19100000017");

			List<String> list = new ArrayList<>();
			list.add("19100000017");

			// 第一次查询
			int count1 = mapper.countByMobiles(list);
			Assertions.assertEquals(1, count1);
			Assertions.assertEquals("19100000017", list.get(0));

			// 第二次查询（参数已经被回滚为明文，应该能再次加密并查询成功）
			int count2 = mapper.countByMobiles(list);
			Assertions.assertEquals(1, count2);
			Assertions.assertEquals("19100000017", list.get(0));
		}
	}

}
