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

package org.ballcat.mybatisplus.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;

/**
 * ON DUPLICATE KEY UPDATE 时使用调用方指定的列值进行更新。
 *
 * <p>
 * <b>安全警告</b>：{@code columns.list} 中每个条目的 {@code val} 字段通过 MyBatis {@code ${}} 直接拼入 SQL，
 * 不经过 PreparedStatement 参数化处理。<br>
 * <b>严禁将任何用户可控输入赋值给 {@code val}</b>，否则将导致 SQL 注入（OWASP A03）。 {@code val}
 * 只能来自框架元数据、常量或经过严格校验的可信内部数据。
 *
 * @author lingting 2020/5/27 11:47
 */
public class InsertOrUpdateFieldByBatch extends BaseInsertBatch {

	private static final String SQL = "<script>insert into %s %s values %s</script>";

	protected InsertOrUpdateFieldByBatch() {
		super("insertOrUpdateFieldByBatch");
	}

	protected InsertOrUpdateFieldByBatch(String methodName) {
		super(methodName);
	}

	@Override
	protected String getSql() {
		return SQL;
	}

	@Override
	protected String prepareValuesSqlForMysqlBatch(TableInfo tableInfo) {
		StringBuilder sql = super.prepareValuesBuildSqlForMysqlBatch(tableInfo);
		sql.append(" ON DUPLICATE KEY UPDATE ")
			// 如果模式为 不忽略设置的字段
			.append("<if test=\"!columns.ignore\">")
			.append("<foreach collection=\"columns.list\" item=\"item\" index=\"index\" separator=\",\" >")
			.append("${item.name}=${item.val}")
			.append("</foreach>")
			.append("</if>");

		// 如果模式为 忽略设置的字段
		sql.append("<if test=\"columns.ignore\">")
			.append("<foreach collection=\"columns.back\" item=\"item\" index=\"index\" separator=\",\" >")
			.append("${item}=VALUES(${item})")
			.append("</foreach>")
			.append("</if>");
		return sql.toString();
	}

}
