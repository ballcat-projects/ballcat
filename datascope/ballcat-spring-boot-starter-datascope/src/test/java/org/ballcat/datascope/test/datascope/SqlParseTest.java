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

package org.ballcat.datascope.test.datascope;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import org.ballcat.datascope.DataScope;
import org.ballcat.datascope.handler.DataPermissionHandler;
import org.ballcat.datascope.handler.DefaultDataPermissionHandler;
import org.ballcat.datascope.processor.DataScopeSqlProcessor;
import org.ballcat.datascope.util.SqlParseUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author hccake
 */
class SqlParseTest {

	DataScope tenantDataScope = new TenantDataScope();

	DataPermissionHandler dataPermissionHandler = new DefaultDataPermissionHandler(
			Collections.singletonList(this.tenantDataScope));

	DataScopeSqlProcessor dataScopeSqlProcessor = new DataScopeSqlProcessor();

	@Test
	void delete() {
		assertSql("delete from entity where id = ?", "DELETE FROM entity WHERE id = ? AND entity.tenant_id = 1");
	}

	@Test
	void update() {
		assertSql("update entity set name = ? where id = ?",
				"UPDATE entity SET name = ? WHERE id = ? AND entity.tenant_id = 1");
	}

	@Test
	void selectSingle() {
		// 单表
		assertSql("select * from entity where id = ?", "SELECT * FROM entity WHERE id = ? AND entity.tenant_id = 1");

		assertSql("select * from entity where id = ? or name = ?",
				"SELECT * FROM entity WHERE (id = ? OR name = ?) AND entity.tenant_id = 1");

		assertSql("SELECT * FROM entity WHERE (id = ? OR name = ?)",
				"SELECT * FROM entity WHERE (id = ? OR name = ?) AND entity.tenant_id = 1");

		/* not */
		assertSql("SELECT * FROM entity WHERE not (id = ? OR name = ?)",
				"SELECT * FROM entity WHERE NOT (id = ? OR name = ?) AND entity.tenant_id = 1");
	}

	@Test
	void selectSubSelectIn() {
		/* in */
		assertSql("SELECT * FROM entity e WHERE e.id IN (select e1.id from entity1 e1 where e1.id = ?)",
				"SELECT * FROM entity e WHERE e.id IN (SELECT e1.id FROM entity1 e1 WHERE e1.id = ? AND e1.tenant_id = 1) AND e.tenant_id = 1");
		// 在最前
		assertSql(
				"SELECT * FROM entity e WHERE e.id IN " + "(select e1.id from entity1 e1 where e1.id = ?) and e.id = ?",
				"SELECT * FROM entity e WHERE e.id IN "
						+ "(SELECT e1.id FROM entity1 e1 WHERE e1.id = ? AND e1.tenant_id = 1) AND e.id = ? AND e.tenant_id = 1");
		// 在最后
		assertSql(
				"SELECT * FROM entity e WHERE e.id = ? and e.id IN " + "(select e1.id from entity1 e1 where e1.id = ?)",
				"SELECT * FROM entity e WHERE e.id = ? AND e.id IN "
						+ "(SELECT e1.id FROM entity1 e1 WHERE e1.id = ? AND e1.tenant_id = 1) AND e.tenant_id = 1");
		// 在中间
		assertSql(
				"SELECT * FROM entity e WHERE e.id = ? and e.id IN "
						+ "(select e1.id from entity1 e1 where e1.id = ?) and e.id = ?",
				"SELECT * FROM entity e WHERE e.id = ? AND e.id IN "
						+ "(SELECT e1.id FROM entity1 e1 WHERE e1.id = ? AND e1.tenant_id = 1) AND e.id = ? AND e.tenant_id = 1");
	}

	@Test
	void selectSubSelectEq() {
		/* = */
		assertSql("SELECT * FROM entity e WHERE e.id = (select e1.id from entity1 e1 where e1.id = ?)",
				"SELECT * FROM entity e WHERE e.id = (SELECT e1.id FROM entity1 e1 WHERE e1.id = ? AND e1.tenant_id = 1) AND e.tenant_id = 1");
	}

	@Test
	void selectSubSelectInnerNotEq() {
		/* inner not = */
		assertSql("SELECT * FROM entity e WHERE not (e.id = (select e1.id from entity1 e1 where e1.id = ?))",
				"SELECT * FROM entity e WHERE NOT (e.id = (SELECT e1.id FROM entity1 e1 WHERE e1.id = ? AND e1.tenant_id = 1)) AND e.tenant_id = 1");

		assertSql(
				"SELECT * FROM entity e WHERE not (e.id = (select e1.id from entity1 e1 where e1.id = ?) and e.id = ?)",
				"SELECT * FROM entity e WHERE NOT (e.id = (SELECT e1.id FROM entity1 e1 WHERE e1.id = ? AND e1.tenant_id = 1) AND e.id = ?) AND e.tenant_id = 1");
	}

	@Test
	void selectSubSelectExists() {
		/* EXISTS */
		assertSql("SELECT * FROM entity e WHERE EXISTS (select e1.id from entity1 e1 where e1.id = ?)",
				"SELECT * FROM entity e WHERE EXISTS (SELECT e1.id FROM entity1 e1 WHERE e1.id = ? AND e1.tenant_id = 1) AND e.tenant_id = 1");

		/* NOT EXISTS */
		assertSql("SELECT * FROM entity e WHERE NOT EXISTS (select e1.id from entity1 e1 where e1.id = ?)",
				"SELECT * FROM entity e WHERE NOT EXISTS (SELECT e1.id FROM entity1 e1 WHERE e1.id = ? AND e1.tenant_id = 1) AND e.tenant_id = 1");
	}

	@Test
	void selectSubSelect() {
		/* >= */
		assertSql("SELECT * FROM entity e WHERE e.id >= (select e1.id from entity1 e1 where e1.id = ?)",
				"SELECT * FROM entity e WHERE e.id >= (SELECT e1.id FROM entity1 e1 WHERE e1.id = ? AND e1.tenant_id = 1) AND e.tenant_id = 1");

		/* <= */
		assertSql("SELECT * FROM entity e WHERE e.id <= (select e1.id from entity1 e1 where e1.id = ?)",
				"SELECT * FROM entity e WHERE e.id <= (SELECT e1.id FROM entity1 e1 WHERE e1.id = ? AND e1.tenant_id = 1) AND e.tenant_id = 1");

		/* <> */
		assertSql("SELECT * FROM entity e WHERE e.id <> (select e1.id from entity1 e1 where e1.id = ?)",
				"SELECT * FROM entity e WHERE e.id <> (SELECT e1.id FROM entity1 e1 WHERE e1.id = ? AND e1.tenant_id = 1) AND e.tenant_id = 1");
	}

	@Test
	void selectFromSelect() {
		assertSql(
				"SELECT * FROM (select e.id from entity e WHERE e.id = (select e1.id from entity1 e1 where e1.id = ?))",
				"SELECT * FROM (SELECT e.id FROM entity e WHERE e.id = (SELECT e1.id FROM entity1 e1 WHERE e1.id = ? AND e1.tenant_id = 1) AND e.tenant_id = 1)");
	}

	@Test
	void selectBodySubSelect() {
		assertSql("select t1.col1,(select t2.col2 from t2 t2 where t1.col1=t2.col1) from t1 t1",
				"SELECT t1.col1, (SELECT t2.col2 FROM t2 t2 WHERE t1.col1 = t2.col1 AND t2.tenant_id = 1) FROM t1 t1 WHERE t1.tenant_id = 1");
	}

	@Test
	void selectLeftJoin() {
		// left join
		assertSql("SELECT * FROM entity e " + "left join entity1 e1 on e1.id = e.id " + "WHERE e.id = ? OR e.name = ?",
				"SELECT * FROM entity e " + "LEFT JOIN entity1 e1 ON e1.id = e.id AND e1.tenant_id = 1 "
						+ "WHERE (e.id = ? OR e.name = ?) AND e.tenant_id = 1");

		assertSql(
				"SELECT * FROM entity e " + "left join entity1 e1 on e1.id = e.id " + "WHERE (e.id = ? OR e.name = ?)",
				"SELECT * FROM entity e " + "LEFT JOIN entity1 e1 ON e1.id = e.id AND e1.tenant_id = 1 "
						+ "WHERE (e.id = ? OR e.name = ?) AND e.tenant_id = 1");

		assertSql(
				"SELECT * FROM entity e " + "left join entity1 e1 on e1.id = e.id "
						+ "left join entity2 e2 on e1.id = e2.id",
				"SELECT * FROM entity e " + "LEFT JOIN entity1 e1 ON e1.id = e.id AND e1.tenant_id = 1 "
						+ "LEFT JOIN entity2 e2 ON e1.id = e2.id AND e2.tenant_id = 1 " + "WHERE e.tenant_id = 1");
	}

	@Test
	void selectRightJoin() {
		// right join
		assertSql("SELECT * FROM entity e " + "right join entity1 e1 on e1.id = e.id", "SELECT * FROM entity e "
				+ "RIGHT JOIN entity1 e1 ON e1.id = e.id AND e.tenant_id = 1 " + "WHERE e1.tenant_id = 1");

		assertSql("SELECT * FROM with_as_1 e " + "right join entity1 e1 on e1.id = e.id",
				"SELECT * FROM with_as_1 e " + "RIGHT JOIN entity1 e1 ON e1.id = e.id " + "WHERE e1.tenant_id = 1");

		assertSql("SELECT * FROM entity e " + "right join entity1 e1 on e1.id = e.id " + "WHERE e.id = ? OR e.name = ?",
				"SELECT * FROM entity e " + "RIGHT JOIN entity1 e1 ON e1.id = e.id AND e.tenant_id = 1 "
						+ "WHERE (e.id = ? OR e.name = ?) AND e1.tenant_id = 1");

		assertSql(
				"SELECT * FROM entity e " + "right join entity1 e1 on e1.id = e.id "
						+ "right join entity2 e2 on e1.id = e2.id ",
				"SELECT * FROM entity e " + "RIGHT JOIN entity1 e1 ON e1.id = e.id AND e.tenant_id = 1 "
						+ "RIGHT JOIN entity2 e2 ON e1.id = e2.id AND e1.tenant_id = 1 " + "WHERE e2.tenant_id = 1");
	}

	@Test
	void selectMixJoin() {
		assertSql(
				"SELECT * FROM entity e " + "right join entity1 e1 on e1.id = e.id "
						+ "left join entity2 e2 on e1.id = e2.id",
				"SELECT * FROM entity e " + "RIGHT JOIN entity1 e1 ON e1.id = e.id AND e.tenant_id = 1 "
						+ "LEFT JOIN entity2 e2 ON e1.id = e2.id AND e2.tenant_id = 1 " + "WHERE e1.tenant_id = 1");

		assertSql(
				"SELECT * FROM entity e " + "left join entity1 e1 on e1.id = e.id "
						+ "right join entity2 e2 on e1.id = e2.id",
				"SELECT * FROM entity e " + "LEFT JOIN entity1 e1 ON e1.id = e.id AND e1.tenant_id = 1 "
						+ "RIGHT JOIN entity2 e2 ON e1.id = e2.id AND e1.tenant_id = 1 " + "WHERE e2.tenant_id = 1");

		assertSql(
				"SELECT * FROM entity e " + "left join entity1 e1 on e1.id = e.id "
						+ "inner join entity2 e2 on e1.id = e2.id",
				"SELECT * FROM entity e " + "LEFT JOIN entity1 e1 ON e1.id = e.id AND e1.tenant_id = 1 "
						+ "INNER JOIN entity2 e2 ON e1.id = e2.id AND e.tenant_id = 1 AND e2.tenant_id = 1");
	}

	@Test
	void selectJoinSubSelect() {
		assertSql("select * from (select * from entity) e1 " + "left join entity2 e2 on e1.id = e2.id",
				"SELECT * FROM (SELECT * FROM entity WHERE entity.tenant_id = 1) e1 "
						+ "LEFT JOIN entity2 e2 ON e1.id = e2.id AND e2.tenant_id = 1");

		assertSql("select * from entity1 e1 " + "left join (select * from entity2) e2 " + "on e1.id = e2.id",
				"SELECT * FROM entity1 e1 " + "LEFT JOIN (SELECT * FROM entity2 WHERE entity2.tenant_id = 1) e2 "
						+ "ON e1.id = e2.id " + "WHERE e1.tenant_id = 1");
	}

	@Test
	void selectSubJoin() {

		assertSql("select * FROM " + "(entity1 e1 right JOIN entity2 e2 ON e1.id = e2.id)",
				"SELECT * FROM " + "(entity1 e1 RIGHT JOIN entity2 e2 ON e1.id = e2.id AND e1.tenant_id = 1) "
						+ "WHERE e2.tenant_id = 1");

		assertSql("select * FROM " + "(entity1 e1 LEFT JOIN entity2 e2 ON e1.id = e2.id)",
				"SELECT * FROM " + "(entity1 e1 LEFT JOIN entity2 e2 ON e1.id = e2.id AND e2.tenant_id = 1) "
						+ "WHERE e1.tenant_id = 1");

		assertSql(
				"select * FROM " + "(entity1 e1 LEFT JOIN entity2 e2 ON e1.id = e2.id) "
						+ "right join entity3 e3 on e1.id = e3.id",
				"SELECT * FROM " + "(entity1 e1 LEFT JOIN entity2 e2 ON e1.id = e2.id AND e2.tenant_id = 1) "
						+ "RIGHT JOIN entity3 e3 ON e1.id = e3.id AND e1.tenant_id = 1 " + "WHERE e3.tenant_id = 1");

		assertSql(
				"select * FROM entity e " + "LEFT JOIN (entity1 e1 right join entity2 e2 ON e1.id = e2.id) "
						+ "on e.id = e2.id",
				"SELECT * FROM entity e "
						+ "LEFT JOIN (entity1 e1 RIGHT JOIN entity2 e2 ON e1.id = e2.id AND e1.tenant_id = 1) "
						+ "ON e.id = e2.id AND e2.tenant_id = 1 " + "WHERE e.tenant_id = 1");

		assertSql(
				"select * FROM entity e " + "LEFT JOIN (entity1 e1 left join entity2 e2 ON e1.id = e2.id) "
						+ "on e.id = e2.id",
				"SELECT * FROM entity e "
						+ "LEFT JOIN (entity1 e1 LEFT JOIN entity2 e2 ON e1.id = e2.id AND e2.tenant_id = 1) "
						+ "ON e.id = e2.id AND e1.tenant_id = 1 " + "WHERE e.tenant_id = 1");

		assertSql(
				"select * FROM entity e " + "RIGHT JOIN (entity1 e1 left join entity2 e2 ON e1.id = e2.id) "
						+ "on e.id = e2.id",
				"SELECT * FROM entity e "
						+ "RIGHT JOIN (entity1 e1 LEFT JOIN entity2 e2 ON e1.id = e2.id AND e2.tenant_id = 1) "
						+ "ON e.id = e2.id AND e.tenant_id = 1 " + "WHERE e1.tenant_id = 1");
	}

	@Test
	void selectLeftJoinMultipleTrailingOn() {
		// 多个 on 尾缀的
		assertSql(
				"SELECT * FROM entity e " + "LEFT JOIN entity1 e1 " + "LEFT JOIN entity2 e2 ON e2.id = e1.id "
						+ "ON e1.id = e.id " + "WHERE (e.id = ? OR e.NAME = ?)",
				"SELECT * FROM entity e " + "LEFT JOIN entity1 e1 "
						+ "LEFT JOIN entity2 e2 ON e2.id = e1.id AND e2.tenant_id = 1 "
						+ "ON e1.id = e.id AND e1.tenant_id = 1 "
						+ "WHERE (e.id = ? OR e.NAME = ?) AND e.tenant_id = 1");

		assertSql(
				"SELECT * FROM entity e " + "LEFT JOIN entity1 e1 " + "LEFT JOIN with_as_A e2 ON e2.id = e1.id "
						+ "ON e1.id = e.id " + "WHERE (e.id = ? OR e.NAME = ?)",
				"SELECT * FROM entity e " + "LEFT JOIN entity1 e1 " + "LEFT JOIN with_as_A e2 ON e2.id = e1.id "
						+ "ON e1.id = e.id AND e1.tenant_id = 1 "
						+ "WHERE (e.id = ? OR e.NAME = ?) AND e.tenant_id = 1");
	}

	@Test
	void testInnerJoin() {
		// inner join
		assertSql("SELECT * FROM entity e inner join entity1 e1 on e1.id = e.id WHERE e.id = ? OR e.name = ?",
				"SELECT * FROM entity e "
						+ "INNER JOIN entity1 e1 ON e1.id = e.id AND e.tenant_id = 1 AND e1.tenant_id = 1 "
						+ "WHERE e.id = ? OR e.name = ?");

		assertSql("SELECT * FROM entity e inner join entity1 e1 on e1.id = e.id WHERE (e.id = ? OR e.name = ?)",
				"SELECT * FROM entity e "
						+ "INNER JOIN entity1 e1 ON e1.id = e.id AND e.tenant_id = 1 AND e1.tenant_id = 1 "
						+ "WHERE (e.id = ? OR e.name = ?)");
	}

	@Test
	void testJoin() {
		// inner join
		assertSql("SELECT * FROM entity e join entity1 e1 on e1.id = e.id WHERE e.id = ? OR e.name = ?",
				"SELECT * FROM entity e " + "JOIN entity1 e1 ON e1.id = e.id AND e.tenant_id = 1 AND e1.tenant_id = 1 "
						+ "WHERE e.id = ? OR e.name = ?");

		assertSql("SELECT * FROM entity e join entity1 e1 on e1.id = e.id WHERE (e.id = ? OR e.name = ?)",
				"SELECT * FROM entity e " + "JOIN entity1 e1 ON e1.id = e.id AND e.tenant_id = 1 AND e1.tenant_id = 1 "
						+ "WHERE (e.id = ? OR e.name = ?)");
	}

	@Test
	void testSimpleJoin() {
		// 隐式内连接
		assertSql("SELECT * FROM entity,entity1 " + "WHERE entity.id = entity1.id", "SELECT * FROM entity, entity1 "
				+ "WHERE entity.id = entity1.id AND entity.tenant_id = 1 AND entity1.tenant_id = 1");

		// SubJoin with 隐式内连接
		assertSql("SELECT * FROM (entity,entity1) " + "WHERE entity.id = entity1.id", "SELECT * FROM (entity, entity1) "
				+ "WHERE entity.id = entity1.id " + "AND entity.tenant_id = 1 AND entity1.tenant_id = 1");

		assertSql(
				"SELECT * FROM ((entity,entity1),entity2) " + "WHERE entity.id = entity1.id and entity.id = entity2.id",
				"SELECT * FROM ((entity, entity1), entity2) "
						+ "WHERE entity.id = entity1.id AND entity.id = entity2.id "
						+ "AND entity.tenant_id = 1 AND entity1.tenant_id = 1 AND entity2.tenant_id = 1");

		assertSql(
				"SELECT * FROM (entity,(entity1,entity2)) " + "WHERE entity.id = entity1.id and entity.id = entity2.id",
				"SELECT * FROM (entity, (entity1, entity2)) "
						+ "WHERE entity.id = entity1.id AND entity.id = entity2.id "
						+ "AND entity.tenant_id = 1 AND entity1.tenant_id = 1 AND entity2.tenant_id = 1");

		// 沙雕的括号写法
		assertSql("SELECT * FROM (((entity,entity1))) " + "WHERE entity.id = entity1.id",
				"SELECT * FROM (((entity, entity1))) " + "WHERE entity.id = entity1.id "
						+ "AND entity.tenant_id = 1 AND entity1.tenant_id = 1");
	}

	@Test
	void selectWithAs() {
		assertSql("with with_as_A as (select * from entity) select * from with_as_A",
				"WITH with_as_A AS (SELECT * FROM entity WHERE entity.tenant_id = 1) SELECT * FROM with_as_A");
	}

	/**
	 * 4.4 版本 jsqlParse ur 做为表别名会解析失败
	 */
	@Test
	void testJsqlParseAlias() {
		String sql = "SELECT\n" + "r.id, r.name, r.code, r.type, r.scope_type, r.scope_resources\n" + "FROM\n"
				+ "sys_user_role ur\n" + "left join\n" + "sys_role r\n" + "on r.code = ur.role_code\n"
				+ "WHERE ur.user_id = ?\n" + "and r.deleted = 0";
		Assertions.assertDoesNotThrow(
				() -> this.dataScopeSqlProcessor.parserSingle(sql, this.dataPermissionHandler.dataScopes()));

	}

	void assertSql(String sql, String targetSql) {
		String parsedSql = this.dataScopeSqlProcessor.parserSingle(sql, this.dataPermissionHandler.dataScopes());
		Assertions.assertEquals(targetSql, parsedSql);
	}

	static class TenantDataScope implements DataScope {

		final String columnName = "tenant_id";

		private static final Set<String> TABLE_NAMES = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		static {
			TABLE_NAMES.addAll(Arrays.asList("entity", "entity1", "entity2", "entity3", "t1", "t2"));
		}

		@Override
		public String getResource() {
			return "tenant";
		}

		@Override
		public boolean includes(String tableName) {
			return TABLE_NAMES.contains(tableName);
		}

		@Override
		public Expression getExpression(String tableName, Alias tableAlias) {
			Column column = SqlParseUtils.getAliasColumn(tableName, tableAlias, this.columnName);
			return new EqualsTo(column, new LongValue("1"));
		}

	}

}
