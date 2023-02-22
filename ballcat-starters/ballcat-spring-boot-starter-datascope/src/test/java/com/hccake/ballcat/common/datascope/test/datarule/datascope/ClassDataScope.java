package com.hccake.ballcat.common.datascope.test.datarule.datascope;

import com.hccake.ballcat.common.datascope.DataScope;
import com.hccake.ballcat.common.datascope.test.datarule.user.LoginUser;
import com.hccake.ballcat.common.datascope.test.datarule.user.LoginUserHolder;
import com.hccake.ballcat.common.datascope.test.datarule.user.UserRoleType;
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
 * 班级维度的数据权限控制
 *
 * @author hccake
 */
public class ClassDataScope implements DataScope {

	public static final String RESOURCE_NAME = "class";

	final String columnId = "class_name";

	private final Set<String> tableNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

	public ClassDataScope() {
		tableNames.addAll(Arrays.asList("h2student", "h2teacher"));
	}

	@Override
	public String getResource() {
		return RESOURCE_NAME;
	}

	@Override
	public boolean includes(String tableName) {
		// 使用 new TreeSet<>(String.CASE_INSENSITIVE_ORDER) 的形式判断，可忽略表名大小写
		return tableNames.contains(tableName);
	}

	@Override
	public Expression getExpression(String tableName, Alias tableAlias) {
		LoginUser loginUser = LoginUserHolder.get();

		// 如果当前登录用户为空，或者是老师，但是没有任何班级权限
		if (loginUser == null || (UserRoleType.TEACHER.equals(loginUser.getUserRoleType())
				&& CollectionUtils.isEmpty(loginUser.getClassNameList()))) {
			// where 1 = 2 永不满足
			return new EqualsTo(new LongValue(1), new LongValue(2));
		}

		// 如果是学生，则不控制，因为学生的权限会在 StudentDataScope 中处理
		if (UserRoleType.STUDENT.equals(loginUser.getUserRoleType())) {
			return null;
		}

		// 提取当前登录用户拥有的班级权限
		List<Expression> list = loginUser.getClassNameList()
			.stream()
			.map(StringValue::new)
			.collect(Collectors.toList());
		Column column = new Column(tableAlias == null ? columnId : tableAlias.getName() + "." + columnId);
		ExpressionList expressionList = new ExpressionList();
		expressionList.setExpressions(list);
		return new InExpression(column, expressionList);
	}

}
