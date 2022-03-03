package com.hccake.ballcat.common.datascope.test.datarule.datascope;

import com.hccake.ballcat.common.datascope.DataScope;
import com.hccake.ballcat.common.datascope.test.datarule.user.LoginUser;
import com.hccake.ballcat.common.datascope.test.datarule.user.LoginUserHolder;
import com.hccake.ballcat.common.datascope.util.CollectionUtils;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author hccake
 */
public class ClassDataScope implements DataScope {

	public static final String RESOURCE_NAME = "class";

	final String columnId = "class_name";

	@Override
	public String getResource() {
		return RESOURCE_NAME;
	}

	@Override
	public Collection<String> getTableNames() {
		Set<String> tableNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		tableNames.addAll(Arrays.asList("h2student", "h2teacher"));
		return tableNames;
	}

	@Override
	public Expression getExpression(String tableName, Alias tableAlias) {
		LoginUser loginUser = LoginUserHolder.get();
		if (loginUser == null || CollectionUtils.isEmpty(loginUser.getClassNameList())) {
			// 永不满足
			return new EqualsTo(new LongValue(1), new LongValue(2));
		}

		// 提取当前登录用户拥有的班级权限
		List<Expression> list = loginUser.getClassNameList().stream().map(StringValue::new)
				.collect(Collectors.toList());
		Column column = new Column(tableAlias == null ? columnId : tableAlias.getName() + "." + columnId);
		ExpressionList expressionList = new ExpressionList();
		expressionList.setExpressions(list);
		return new InExpression(column, expressionList);
	}

}
