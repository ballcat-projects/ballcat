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

package org.ballcat.datascope.util;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

/**
 * SQL 解析工具类
 *
 * @author hccake
 */
public final class SqlParseUtils {

	private SqlParseUtils() {
	}

	private static final String MYSQL_ESCAPE_CHARACTER = "`";

	/**
	 * 兼容 mysql 转义表名 `t_xxx`
	 * @param tableName 表名
	 * @return 去除转移字符后的表名
	 */
	public static String getTableName(String tableName) {
		if (tableName.startsWith(MYSQL_ESCAPE_CHARACTER) && tableName.endsWith(MYSQL_ESCAPE_CHARACTER)) {
			tableName = tableName.substring(1, tableName.length() - 1);
		}
		return tableName;
	}

	/**
	 * 根据当前表是否有别名，动态对字段名前添加表别名 eg. 表名： table_1 as t 原始字段：column1 返回： t.column1
	 * @param table 表信息
	 * @param columnName 字段名
	 * @return 原始字段名，或者添加了表别名的字段名
	 */
	public static Column getAliasColumn(Table table, String columnName) {
		return getAliasColumn(table.getName(), table.getAlias(), columnName);
	}

	/**
	 * 根据当前表是否有别名，动态对字段名前添加表别名 eg. 表名： table_1 as t 原始字段：column1 返回： t.column1
	 * @param tableName 表名
	 * @param tableAlias 别别名
	 * @param columnName 字段名
	 * @return 原始字段名，或者添加了表别名的字段名
	 */
	public static Column getAliasColumn(String tableName, Alias tableAlias, String columnName) {
		StringBuilder columnBuilder = new StringBuilder();
		// 为了兼容隐式内连接，没有别名时条件就需要加上表名
		if (tableAlias != null) {
			columnBuilder.append(tableAlias.getName());
		}
		else {
			columnBuilder.append(tableName);
		}
		columnBuilder.append(".").append(columnName);
		return new Column(columnBuilder.toString());
	}

}
