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

import java.util.List;
import java.util.function.Predicate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 从 {@link InsertBatchSomeColumn} 复制
 *
 * @author lingting 2021/2/23 15:32
 */
public class InsertBatchSomeColumnByCollection extends AbstractMethod {

	private static final String DEFAULT_METHOD_NAME = "insertBatchSomeColumn";

	public InsertBatchSomeColumnByCollection() {
		super(DEFAULT_METHOD_NAME);
	}

	public InsertBatchSomeColumnByCollection(Predicate<TableFieldInfo> predicate) {
		this(DEFAULT_METHOD_NAME, predicate);
	}

	/**
	 * 自定义 mapper 方法名
	 */
	public InsertBatchSomeColumnByCollection(String methodName, Predicate<TableFieldInfo> predicate) {
		super(methodName);
		this.predicate = predicate;
	}

	/**
	 * 字段筛选条件
	 */
	@Setter
	@Accessors(chain = true)
	private Predicate<TableFieldInfo> predicate;

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
		SqlMethod sqlMethod = SqlMethod.INSERT_ONE;
		List<TableFieldInfo> fieldList = tableInfo.getFieldList();
		String insertSqlColumn = tableInfo.getKeyInsertSqlColumn(true, null, false)
				+ this.filterTableFieldInfo(fieldList, this.predicate, TableFieldInfo::getInsertSqlColumn, EMPTY);
		String columnScript = LEFT_BRACKET + insertSqlColumn.substring(0, insertSqlColumn.length() - 1) + RIGHT_BRACKET;
		String insertSqlProperty = tableInfo.getKeyInsertSqlProperty(true, ENTITY_DOT, false)
				+ this.filterTableFieldInfo(fieldList, this.predicate, i -> i.getInsertSqlProperty(ENTITY_DOT), EMPTY);
		insertSqlProperty = LEFT_BRACKET + insertSqlProperty.substring(0, insertSqlProperty.length() - 1)
				+ RIGHT_BRACKET;
		// 从 list 改为 collection. 允许传入除 list外的参数类型
		String valuesScript = SqlScriptUtils.convertForeach(insertSqlProperty, "collection", null, ENTITY, COMMA);
		String keyProperty = null;
		String keyColumn = null;
		// 表包含主键处理逻辑,如果不包含主键当普通字段处理
		if (tableInfo.havePK()) {
			if (tableInfo.getIdType() == IdType.AUTO) {
				/* 自增主键 */
				keyGenerator = Jdbc3KeyGenerator.INSTANCE;
				keyProperty = tableInfo.getKeyProperty();
				keyColumn = tableInfo.getKeyColumn();
			}
			else {
				if (null != tableInfo.getKeySequence()) {
					keyGenerator = TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, this.builderAssistant);
					keyProperty = tableInfo.getKeyProperty();
					keyColumn = tableInfo.getKeyColumn();
				}
			}
		}
		String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), columnScript, valuesScript);
		SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, modelClass);
		return this.addInsertMappedStatement(mapperClass, modelClass, this.methodName, sqlSource, keyGenerator,
				keyProperty, keyColumn);
	}

}
