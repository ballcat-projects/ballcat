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

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.ballcat.datascope.DataScope;
import org.ballcat.datascope.handler.DataPermissionHandler;
import org.ballcat.datascope.handler.DefaultDataPermissionHandler;
import org.ballcat.datascope.holder.DataScopeMatchNumHolder;
import org.ballcat.datascope.processor.DataScopeSqlProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Hccake 2020/9/28
 */
class DataScopeMatchTest {

	DataScope dataScope = new DataScope() {
		final String columnId = "order_id";

		@Override
		public String getResource() {
			return "order";
		}

		@Override
		public boolean includes(String tableName) {
			return "t_order".equalsIgnoreCase(tableName) || "t_order_info".equalsIgnoreCase(tableName);
		}

		@Override
		public Expression getExpression(String tableName, Alias tableAlias) {
			Column column = new Column(tableAlias == null ? this.columnId : tableAlias.getName() + "." + this.columnId);
			ExpressionList expressionList = new ExpressionList();
			expressionList.setExpressions(Arrays.asList(new StringValue("1"), new StringValue("2")));
			return new InExpression(column, expressionList);
		}
	};

	DataPermissionHandler dataPermissionHandler = new DefaultDataPermissionHandler(
			Collections.singletonList(this.dataScope));

	DataScopeSqlProcessor dataScopeSqlProcessor = new DataScopeSqlProcessor();

	@Test
	void testMatchNum() {
		String sql = "select o.order_id,o.order_name,oi.order_price "
				+ "from t_ORDER o left join t_order_info oi on o.order_id = oi.order_id "
				+ "where oi.order_price > 100";

		DataScopeMatchNumHolder.initMatchNum();
		try {
			String parseSql = this.dataScopeSqlProcessor.parserSingle(sql, this.dataPermissionHandler.dataScopes());
			System.out.println(parseSql);

			Integer matchNum = DataScopeMatchNumHolder.pollMatchNum();
			Assertions.assertEquals(2, matchNum, "sql 数据权限匹配计数异常");

		}
		finally {
			DataScopeMatchNumHolder.removeIfEmpty();
		}

	}

	@Test
	void testNoMatch() {
		String sql = "select o.order_id,o.order_name,oi.order_price "
				+ "from t_ORDER_1 o left join t_order_info_1 oi on o.order_id = oi.order_id "
				+ "where oi.order_price > 100";
		DataScopeMatchNumHolder.initMatchNum();
		try {
			String parseSql = this.dataScopeSqlProcessor.parserSingle(sql, this.dataPermissionHandler.dataScopes());
			System.out.println(parseSql);

			Integer matchNum = DataScopeMatchNumHolder.pollMatchNum();
			Assertions.assertEquals(0, matchNum, "sql 数据权限匹配计数异常");

		}
		finally {
			DataScopeMatchNumHolder.removeIfEmpty();
		}

	}

	/**
	 * 嵌套进行 matchNumber 匹配
	 */
	@Test
	void testNestedMatchNum() {
		String sql = "select o.order_id,o.order_name,oi.order_price "
				+ "from t_ORDER o left join t_order_info oi on o.order_id = oi.order_id "
				+ "where oi.order_price > 100";

		DataScopeMatchNumHolder.initMatchNum();
		try {

			testNoMatch();

			String parseSql = this.dataScopeSqlProcessor.parserSingle(sql, this.dataPermissionHandler.dataScopes());
			System.out.println(parseSql);

			Integer matchNum = DataScopeMatchNumHolder.pollMatchNum();
			Assertions.assertEquals(2, matchNum, "sql 数据权限匹配计数异常");

		}
		finally {
			DataScopeMatchNumHolder.removeIfEmpty();
		}

	}

}
