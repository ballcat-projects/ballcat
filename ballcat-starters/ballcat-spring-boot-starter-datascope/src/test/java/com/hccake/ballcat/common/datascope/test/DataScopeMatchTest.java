package com.hccake.ballcat.common.datascope.test;

import com.hccake.ballcat.common.datascope.DataScope;
import com.hccake.ballcat.common.datascope.handler.DataPermissionHandler;
import com.hccake.ballcat.common.datascope.handler.DefaultDataPermissionHandler;
import com.hccake.ballcat.common.datascope.holder.DataScopeMatchNumHolder;
import com.hccake.ballcat.common.datascope.processor.DataScopeSqlProcessor;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Hccake 2020/9/28
 * @version 1.0
 */
class DataScopeMatchTest {

	DataScope dataScope = new DataScope() {
		final String columnId = "order_id";

		@Override
		public String getResource() {
			return "order";
		}

		@Override
		public Collection<String> getTableNames() {
			Set<String> tableNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
			tableNames.addAll(Arrays.asList("t_order", "t_order_info"));
			return tableNames;
		}

		@Override
		public Expression getExpression(String tableName, Alias tableAlias) {
			Column column = new Column(tableAlias == null ? columnId : tableAlias.getName() + "." + columnId);
			ExpressionList expressionList = new ExpressionList();
			expressionList.setExpressions(Arrays.asList(new StringValue("1"), new StringValue("2")));
			return new InExpression(column, expressionList);
		}
	};

	DataPermissionHandler dataPermissionHandler = new DefaultDataPermissionHandler(
			Collections.singletonList(dataScope));

	DataScopeSqlProcessor dataScopeSqlProcessor = new DataScopeSqlProcessor();

	@Test
	void testRight() {
		String sql = "select o.order_id,o.order_name,oi.order_price "
				+ "from t_ORDER o left join t_order_info oi on o.order_id = oi.order_id "
				+ "where oi.order_price > 100";

		String parseSql = dataScopeSqlProcessor.parserSingle(sql, dataPermissionHandler.dataScopes());
		System.out.println(parseSql);

		String trueSql = "SELECT o.order_id, o.order_name, oi.order_price FROM t_ORDER o LEFT JOIN t_order_info oi ON o.order_id = oi.order_id AND oi.order_id IN ('1', '2') WHERE oi.order_price > 100 AND o.order_id IN ('1', '2')";
		Assertions.assertEquals(trueSql, parseSql, "sql 数据权限解析异常");
	}

	@Test
	void testMatchNum() {
		String sql = "select o.order_id,o.order_name,oi.order_price "
				+ "from t_ORDER o left join t_order_info oi on o.order_id = oi.order_id "
				+ "where oi.order_price > 100";

		DataScopeMatchNumHolder.initMatchNum();
		try {
			String parseSql = dataScopeSqlProcessor.parserSingle(sql, dataPermissionHandler.dataScopes());
			System.out.println(parseSql);

			int matchNum = DataScopeMatchNumHolder.getMatchNum();
			Assertions.assertEquals(2, matchNum, "sql 数据权限匹配计数异常");

		}
		finally {
			DataScopeMatchNumHolder.remove();
		}

	}

	@Test
	void testNoMatch() {
		String sql = "select o.order_id,o.order_name,oi.order_price "
				+ "from t_ORDER_1 o left join t_order_info_1 oi on o.order_id = oi.order_id "
				+ "where oi.order_price > 100";
		DataScopeMatchNumHolder.initMatchNum();
		try {
			String parseSql = dataScopeSqlProcessor.parserSingle(sql, dataPermissionHandler.dataScopes());
			System.out.println(parseSql);

			int matchNum = DataScopeMatchNumHolder.getMatchNum();
			Assertions.assertEquals(0, matchNum, "sql 数据权限匹配计数异常");

		}
		finally {
			DataScopeMatchNumHolder.remove();
		}

	}

}
