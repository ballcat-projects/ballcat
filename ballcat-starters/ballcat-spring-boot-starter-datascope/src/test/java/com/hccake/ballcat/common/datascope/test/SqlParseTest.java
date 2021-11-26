package com.hccake.ballcat.common.datascope.test;

import com.hccake.ballcat.common.datascope.DataScope;
import com.hccake.ballcat.common.datascope.handler.DataPermissionHandler;
import com.hccake.ballcat.common.datascope.handler.DefaultDataPermissionHandler;
import com.hccake.ballcat.common.datascope.processor.DataScopeSqlProcessor;
import com.hccake.ballcat.common.datascope.util.SqlParseUtils;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author hccake
 */
public class SqlParseTest {

	DataScope tenantDataScope = new DataScope() {
		final String columnName = "tenant_id";

		@Override
		public String getResource() {
			return "tenant";
		}

		@Override
		public Collection<String> getTableNames() {
			Set<String> tableNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
			tableNames.addAll(Arrays.asList("entity", "entity1", "entity2", "entity3", "t1", "t2"));
			return tableNames;
		}

		@Override
		public Expression getExpression(String tableName, Alias tableAlias) {
			Column column = SqlParseUtils.getAliasColumn(tableName, tableAlias, columnName);
			return new EqualsTo(column, new LongValue("1"));
		}
	};

	DataPermissionHandler dataPermissionHandler = new DefaultDataPermissionHandler(
			Collections.singletonList(tenantDataScope));

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
	void selectInnerJoin() {
		// inner join
		assertSql("SELECT * FROM entity e " + "inner join entity1 e1 on e1.id = e.id " + "WHERE e.id = ? OR e.name = ?",
				"SELECT * FROM entity e "
						+ "INNER JOIN entity1 e1 ON e1.id = e.id AND e.tenant_id = 1 AND e1.tenant_id = 1 "
						+ "WHERE e.id = ? OR e.name = ?");

		assertSql(
				"SELECT * FROM entity e " + "inner join entity1 e1 on e1.id = e.id " + "WHERE (e.id = ? OR e.name = ?)",
				"SELECT * FROM entity e "
						+ "INNER JOIN entity1 e1 ON e1.id = e.id AND e.tenant_id = 1 AND e1.tenant_id = 1 "
						+ "WHERE (e.id = ? OR e.name = ?)");

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

	void assertSql(String sql, String targetSql) {
		assertThat(dataScopeSqlProcessor.parserSingle(sql, dataPermissionHandler.dataScopes())).isEqualTo(targetSql);
	}

}
