package com.hccake.ballcat.common.datascope.test;

import com.hccake.ballcat.common.datascope.DataScope;
import com.hccake.ballcat.common.datascope.handler.DataPermissionHandler;
import com.hccake.ballcat.common.datascope.processor.DataScopeSqlProcessor;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author Hccake 2020/9/28
 * @version 1.0
 */
public class SqlParseTest {

	@Test
	public void test() {
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

		DataPermissionHandler dataPermissionHandler = new DataPermissionHandler() {
			@Override
			public List<DataScope> dataScopes() {
				List<DataScope> list = new ArrayList<>();
				list.add(dataScope);
				return list;
			}

			@Override
			public boolean ignorePermissionControl() {
				return false;
			}
		};

		DataScopeSqlProcessor dataScopeSqlProcessor = new DataScopeSqlProcessor();

		// DataScopeHolder.putDataScope("order", dataScope);

		String sql = "select o.order_id,o.order_name,oi.order_price "
				+ "from t_ORDER o left join t_order_info oi on o.order_id = oi.order_id "
				+ "where oi.order_price > 100";

		dataScopeSqlProcessor.parserSingle(sql, dataPermissionHandler.dataScopes());
	}

}
