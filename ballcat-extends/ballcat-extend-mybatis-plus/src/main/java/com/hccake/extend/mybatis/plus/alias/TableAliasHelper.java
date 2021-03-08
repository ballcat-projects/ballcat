package com.hccake.extend.mybatis.plus.alias;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 表别名辅助类
 *
 * @author Hccake 2021/1/14
 * @version 1.0
 */
public class TableAliasHelper {

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
				throw new RuntimeException("[tableAliasSelectSql] No TableAlias annotations found in class: " + clazz);
			}
			tableAlias = annotation.value();
			TABLE_ALIAS_CACHE.put(clazz, tableAlias);
		}
		return tableAlias;
	}

}
