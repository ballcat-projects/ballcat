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

package org.ballcat.mybatisplus.alias;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 表别名辅助类
 *
 * @author Hccake 2021/1/14
 *
 */
public final class TableAliasHelper {

	private TableAliasHelper() {
	}

	private static final String COMMA = ",";

	private static final String DOT = ".";

	/**
	 * 存储类对应的表别名
	 */
	private static final Map<Class<?>, String> TABLE_ALIAS_CACHE = new ConcurrentHashMap<>();

	/**
	 * 储存类对应的带别名的查询字段
	 */
	private static final Map<Class<?>, String> TABLE_ALIAS_SELECT_COLUMNS_CACHE = new ConcurrentHashMap<>();

	/**
	 * 带别名的查询字段sql
	 * @param clazz 实体类class
	 * @return sql片段
	 */
	public static String tableAliasSelectSql(Class<?> clazz) {
		String tableAliasSelectSql = TABLE_ALIAS_SELECT_COLUMNS_CACHE.get(clazz);
		if (tableAliasSelectSql == null) {
			String tableAlias = tableAlias(clazz);

			TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
			String allSqlSelect = tableInfo.getAllSqlSelect();
			String[] split = allSqlSelect.split(COMMA);
			StringBuilder stringBuilder = new StringBuilder();
			for (String column : split) {
				stringBuilder.append(tableAlias).append(DOT).append(column).append(COMMA);
			}
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			tableAliasSelectSql = stringBuilder.toString();

			TABLE_ALIAS_SELECT_COLUMNS_CACHE.put(clazz, tableAliasSelectSql);
		}
		return tableAliasSelectSql;
	}

	/**
	 * 获取实体类对应别名
	 * @param clazz 实体类
	 * @return 表别名
	 */
	public static String tableAlias(Class<?> clazz) {
		String tableAlias = TABLE_ALIAS_CACHE.get(clazz);
		if (tableAlias == null) {
			TableAlias annotation = AnnotationUtils.findAnnotation(clazz, TableAlias.class);
			if (annotation == null) {
				throw new TableAliasNotFoundException(
						"[tableAliasSelectSql] No TableAlias annotations found in class: " + clazz);
			}
			tableAlias = annotation.value();
			TABLE_ALIAS_CACHE.put(clazz, tableAlias);
		}
		return tableAlias;
	}

}
