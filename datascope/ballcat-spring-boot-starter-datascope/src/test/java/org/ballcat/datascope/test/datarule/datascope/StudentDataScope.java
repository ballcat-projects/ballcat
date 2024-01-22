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

package org.ballcat.datascope.test.datarule.datascope;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import org.ballcat.datascope.DataScope;
import org.ballcat.datascope.test.datarule.user.LoginUser;
import org.ballcat.datascope.test.datarule.user.LoginUserHolder;
import org.ballcat.datascope.test.datarule.user.UserRoleType;

/**
 * 学生的数据权限控制，学生只能看自己
 *
 * @author hccake
 */
public class StudentDataScope implements DataScope {

	public static final String RESOURCE_NAME = "student";

	private static final Pattern TABLE_NAME_PATTEN = Pattern.compile("^h2student*$");

	@Override
	public String getResource() {
		return RESOURCE_NAME;
	}

	@Override
	public boolean includes(String tableName) {
		// 可以利用正则做匹配
		Matcher matcher = TABLE_NAME_PATTEN.matcher(tableName);
		return matcher.matches();
	}

	@Override
	public Expression getExpression(String tableName, Alias tableAlias) {
		LoginUser loginUser = LoginUserHolder.get();

		// 如果当前登录用户为空
		if (loginUser == null) {
			// where 1 = 2 永不满足
			return new EqualsTo(new LongValue(1), new LongValue(2));
		}

		// 如果是老师则直接放行
		if (UserRoleType.TEACHER.equals(loginUser.getUserRoleType())) {
			return null;
		}

		// 学生只能查到他自己的数据 where id = xx
		Column column = new Column(tableAlias == null ? "id" : tableAlias.getName() + "." + "id");
		return new EqualsTo(column, new LongValue(loginUser.getId()));
	}

}
