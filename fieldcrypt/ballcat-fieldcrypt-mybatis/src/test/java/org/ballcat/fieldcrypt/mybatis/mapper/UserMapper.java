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

package org.ballcat.fieldcrypt.mybatis.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;
import org.ballcat.fieldcrypt.annotation.DecryptResult;
import org.ballcat.fieldcrypt.annotation.Encrypted;
import org.ballcat.fieldcrypt.mybatis.testmodel.User;
import org.ballcat.fieldcrypt.mybatis.testmodel.UserDetailView;

/**
 * 测试用 Mapper.
 *
 * @author Hccake
 * @since 2.0.0
 */
public interface UserMapper {

	@Insert("INSERT INTO t_user(id, name, mobile, email, address) VALUES(#{id}, #{name}, #{mobile}, #{email}, #{address})")
	int insert(User user);

	@Select("SELECT id, name, mobile, email, address FROM t_user WHERE id = #{id}")
	User selectById(@Param("id") Long id);

	@Select("SELECT id, name, mobile, email, address FROM t_user WHERE id = #{0}")
	User selectByIdNoParam(Long id);

	// 参数级注解加密测试（直接使用独立参数）
	@Insert("INSERT INTO t_user(id, name, mobile) VALUES(#{id}, #{name}, #{mobile})")
	int insertRaw(@Param("id") Long id, @Param("name") String name, @Encrypted @Param("mobile") String mobile);

	// Map 参数 + mapKeys 精细控制，仅加密 mobile
	// 仅覆盖到 mobile 的 mapKeys；email/address 在该用例中不出现
	@Insert("INSERT INTO t_user(id, name, mobile) VALUES(#{data.id}, #{data.name}, #{data.mobile})")
	int insertByMap(@Encrypted(mapKeys = { "mobile" }) @Param("data") Map<String, Object> data);

	// Map 参数 + 通配符：对 Map 中所有字符串值进行加密（仅用于插入场景的原始密文断言）
	@Insert("INSERT INTO t_user(id, name, mobile) VALUES(#{data.id}, #{data.name}, #{data.mobile})")
	int insertByMapWildcard(@Encrypted(mapKeys = { "*" }) @Param("data") Map<String, Object> data);

	// 批量(集合)插入，验证集合内部元素实体字段加密
	@Insert({ "<script>", "INSERT INTO t_user(id, name, mobile, email, address) VALUES",
			"<foreach collection='list' item='u' separator=','>",
			"(#{u.id}, #{u.name}, #{u.mobile}, #{u.email}, #{u.address})", "</foreach>", "</script>" })
	int batchInsertList(@Param("list") List<User> list);

	// 数组参数批量插入
	@Insert({ "<script>", "INSERT INTO t_user(id, name, mobile, email, address) VALUES",
			"<foreach collection='arr' item='u' separator=','>",
			"(#{u.id}, #{u.name}, #{u.mobile}, #{u.email}, #{u.address})", "</foreach>", "</script>" })
	int batchInsertArray(@Param("arr") User[] arr);

	// List 批处理插入（与 batchInsertList 类似，提供更语义化的方法名供测试使用）
	@Insert({ "<script>", "INSERT INTO t_user(id, name, mobile, email, address) VALUES",
			"<foreach collection='list' item='u' separator=','>",
			"(#{u.id}, #{u.name}, #{u.mobile}, #{u.email}, #{u.address})", "</foreach>", "</script>" })
	int insertByBatch(@Param("list") List<User> list);

	// 使用一般 Collection<User>（非 List）插入，验证非 List 集合形态下实体字段仍被加密
	@Insert({ "<script>", "INSERT INTO t_user(id, name, mobile, email, address) VALUES",
			"<foreach collection='c' item='u' separator=','>",
			"(#{u.id}, #{u.name}, #{u.mobile}, #{u.email}, #{u.address})", "</foreach>", "</script>" })
	int insertByCollection(@Param("c") Collection<User> collection);

	// 使用 ParamMap 多别名 (param1/arg0/user) 的更新：测试重复加密避免
	@Update("UPDATE t_user SET mobile = #{user.mobile} WHERE id = #{user.id}")
	int updateMobile(@Param("user") User user);

	// 按加密字段删除，参数级注解触发加密
	@Delete("DELETE FROM t_user WHERE mobile = #{mobile}")
	int deleteByMobile(@Encrypted @Param("mobile") String mobile);

	// 数字索引引用测试：不使用 @Param，关闭 useActualParamName 时 ParamNameResolver 生成键 "0","1"；
	// SQL 使用 #{1} 访问第二个参数 (mobile)。第二参数加 @DataShield 注解触发加密。
	@Select("SELECT id, name, mobile, email, address FROM t_user WHERE mobile = #{1}")
	User selectByMobileNumericIndex(@Param("idIgnored") Long idIgnored, @Encrypted String mobile);

	@Select("SELECT id, name, mobile, email, address FROM t_user WHERE mobile = #{param1}")
	User selectByMobileWithRowBounds(RowBounds rb, @Encrypted @Param("mobile") String mobile);

	@Select("SELECT id, name, mobile, email, address FROM t_user ORDER BY id")
	List<User> selectAll();

	// 单参数（无 @Param）字符串加密：验证根参数替换
	@Select("SELECT COUNT(1) FROM t_user WHERE mobile = #{value}")
	int countByMobileSingle(@Encrypted String mobile);

	// 集合字符串参数（@Param 标注）按元素加密
	@Select({ "<script>", "SELECT COUNT(1) FROM t_user WHERE mobile IN",
			"<foreach collection='list' item='m' open='(' separator=',' close=')'>", "#{m}", "</foreach>",
			"</script>" })
	int countByMobiles(@Encrypted @Param("list") List<String> mobiles);

	// 集合字符串参数, 集合元素无 @Encrypted 注解，应该不加密
	@Select({ "<script>", "SELECT COUNT(1) FROM t_user WHERE mobile IN",
			"<foreach collection='list' item='m' open='(' separator=',' close=')'>", "#{m}", "</foreach>",
			"</script>" })
	int countByMobilesWithoutEncrypt(@Param("list") List<String> mobiles);

	// 数组字符串参数按元素加密
	@Select({ "<script>", "SELECT COUNT(1) FROM t_user WHERE mobile IN",
			"<foreach collection='arr' item='m' open='(' separator=',' close=')'>", "#{m}", "</foreach>", "</script>" })
	int countByMobilesArray(@Encrypted @Param("arr") String[] arr);

	// Set<String> 集合参数：用于验证当前实现对非 List 集合不会替换元素（从而无法加密）
	@Select({ "<script>", "SELECT COUNT(1) FROM t_user WHERE mobile IN",
			"<foreach collection='set' item='m' open='(' separator=',' close=')'>", "#{m}", "</foreach>", "</script>" })
	int countByMobilesSet(@Encrypted @Param("set") Set<String> mobiles);

	// Set<String> 集合参数：没有@Encrypted 注解，不应该加密处理
	@Select({ "<script>", "SELECT COUNT(1) FROM t_user WHERE mobile IN",
			"<foreach collection='set' item='m' open='(' separator=',' close=')'>", "#{m}", "</foreach>", "</script>" })
	int countByMobilesSetWithoutEncrypted(@Param("set") Set<String> mobiles);

	// Map 参数 + mapKeys 精细控制，查询计数
	@Select("SELECT COUNT(1) FROM t_user WHERE mobile = #{data.mobile}")
	int countByMap(@Encrypted(mapKeys = { "mobile" }) @Param("data") Map<String, Object> data);

	// Map + 通配符：计数查询（配合 restorePlaintext 验证回滚）
	@Select("SELECT COUNT(1) FROM t_user WHERE mobile = #{data.mobile}")
	int countByMapWildcard(@Encrypted(mapKeys = { "*" }) @Param("data") Map<String, Object> data);

	// Map + 缺失键：指定不存在的键时不应加密任何值
	@Select("SELECT COUNT(1) FROM t_user WHERE mobile = #{data.mobile}")
	int countByMapAbsentKey(@Encrypted(mapKeys = { "not_exist" }) @Param("data") Map<String, Object> data);

	// Map 值为实体（通配符触发实体字段加密）
	@Select("SELECT COUNT(1) FROM t_user WHERE mobile = #{data.user.mobile}")
	int countByMapEntityWildcard(@Encrypted(mapKeys = { "*" }) @Param("data") Map<String, Object> data);

	// 嵌套 Map：当前实现不递归处理嵌套 Map（应无法匹配密文）
	@Select("SELECT COUNT(1) FROM t_user WHERE mobile = #{data.inner.mobile}")
	int countByNestedMap(@Encrypted(mapKeys = { "*" }) @Param("data") Map<String, Object> data);

	// 实体参数计数（用于 restorePlaintext 验证）
	@Select("SELECT COUNT(1) FROM t_user WHERE mobile = #{user.mobile}")
	int countByUser(@Param("user") User user);

	// 未注册算法（用于 failFast 行为验证）
	@Select("SELECT COUNT(1) FROM t_user WHERE mobile = #{mobile}")
	int countByMobileUnknownAlgo(@Encrypted(algo = "__UNKNOWN__") @Param("mobile") String mobile);

	// 查询手机号，应返回解密后的值
	@DecryptResult
	@Select("SELECT mobile FROM t_user WHERE id = #{id}")
	String selectMobileById(@Param("id") Long id);

	// 查询手机号，应返回解密后的值
	@DecryptResult
	@Select("SELECT mobile FROM t_user")
	List<String> selectMobiles();

	// ----- 嵌套与延迟查询 -----
	@Select("SELECT id, name FROM t_user WHERE id = #{id}")
	@Results(id = "UserDetailViewMap",
			value = { @Result(column = "id", property = "id"), @Result(column = "name", property = "name"),
					@Result(property = "user", column = "id",
							one = @One(select = "selectById", fetchType = org.apache.ibatis.mapping.FetchType.LAZY)) })
	UserDetailView selectDetailById(@Param("id") Long id);

	// 用于延迟查询参数加密：按手机号计数
	@Select("SELECT COUNT(1) FROM t_user WHERE mobile = #{mobile}")
	int countByMobileForLazy(@Encrypted @Param("mobile") String mobile);

}
