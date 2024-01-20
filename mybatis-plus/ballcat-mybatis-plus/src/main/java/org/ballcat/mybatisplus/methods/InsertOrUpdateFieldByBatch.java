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
