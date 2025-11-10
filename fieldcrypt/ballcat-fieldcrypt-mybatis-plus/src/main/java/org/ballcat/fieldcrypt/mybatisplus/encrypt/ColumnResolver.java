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

package org.ballcat.fieldcrypt.mybatisplus.encrypt;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;

/**
 * 列参数解析器：将列参数（SFunction 或字符串）解析为 entityClass、propertyName、columnName。 与 ArgEncryptor
 * 解耦，便于复用与测试。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Slf4j
public final class ColumnResolver {

	private ColumnResolver() {
	}

	/** 日志前缀统一管理 */
	private static final String LOG_PREFIX = "FieldCrypt | mp | ";

	/**
	 * 解析列参数为实体类、属性名和列名。
	 * <p>
	 * 支持两种列参数形式：
	 * <ul>
	 * <li>{@code SFunction}（lambda 表达式）：通过 {@link LambdaUtils} 与 {@link TableInfo}
	 * 推导属性与列</li>
	 * <li>{@code String} 列名：使用 {@link TableInfo} 反查属性名</li>
	 * </ul>
	 * @param wrapper 查询包装器，用于获取实体类信息
	 * @param columnArg 列参数，可以是 SFunction 或字符串列名
	 * @return 解析后的列引用对象，包含实体类、属性名和列名
	 */
	public static ColumnRef resolve(Object wrapper, Object columnArg) {
		if (columnArg == null) {
			return null;
		}

		// SFunction path: use LambdaUtils
		if (columnArg instanceof SFunction) {
			SFunction<?, ?> fn = (SFunction<?, ?>) columnArg;

			LambdaMeta meta = LambdaUtils.extract(fn);
			String property = PropertyNamer.methodToProperty(meta.getImplMethodName());

			Class<?> entityClass = extractEntityClassFromWrapper(wrapper);
			if (entityClass == null) {
				entityClass = meta.getInstantiatedClass();
			}

			String column = null;
			if (entityClass != null) {
				column = findColumnViaTableInfo(entityClass, property);
			}
			return new ColumnRef(entityClass, property, column);
		}

		// String column path: treat input as column name only
		if (columnArg instanceof CharSequence) {
			String column = columnArg.toString();
			Class<?> entityClass = extractEntityClassFromWrapper(wrapper);
			String property = findPropertyViaTableInfo(entityClass, column);
			return new ColumnRef(entityClass, property, column);
		}

		return null;
	}

	private static String findColumnViaTableInfo(Class<?> entityClass, String property) {
		try {
			TableInfo ti = TableInfoHelper.getTableInfo(entityClass); // MP 对 null 安全
			if (ti == null || property == null) {
				return null;
			}
			if (property.equals(ti.getKeyProperty())) {
				return ti.getKeyColumn();
			}
			for (TableFieldInfo fi : ti.getFieldList()) {
				if (property.equals(fi.getProperty())) {
					return fi.getColumn();
				}
			}
		}
		catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug(LOG_PREFIX + "resolve column failed entityClass={} property={}", entityClass, property, e);
			}
		}
		return null;
	}

	/**
	 * 根据列名反查属性名（仅按原语义：列名 -> 属性名，不尝试把入参当属性名）。
	 * @param entityClass 实体类
	 * @param column 列名（可能是主键列或普通列）
	 * @return propertyName 或 null
	 */
	private static String findPropertyViaTableInfo(Class<?> entityClass, String column) {
		try {
			TableInfo ti = TableInfoHelper.getTableInfo(entityClass); // MP 对 null 安全
			if (ti == null || column == null) {
				return null;
			}
			if (column.equalsIgnoreCase(ti.getKeyColumn())) {
				return ti.getKeyProperty();
			}
			for (TableFieldInfo fi : ti.getFieldList()) {
				if (column.equalsIgnoreCase(fi.getColumn())) {
					return fi.getProperty();
				}
			}
		}
		catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug(LOG_PREFIX + "resolve property failed entityClass={} column={} ", entityClass, column, e);
			}
		}
		return null;
	}

	private static Class<?> extractEntityClassFromWrapper(Object wrapper) {
		if (wrapper == null) {
			return null;
		}
		// 1) Preferred: call AbstractWrapper.getEntityClass()
		if (wrapper instanceof AbstractWrapper) {
			try {
				@SuppressWarnings("rawtypes")
				AbstractWrapper aw = (AbstractWrapper) wrapper;
				Class<?> ec = aw.getEntityClass();
				if (ec != null) {
					return ec;
				}
			}
			catch (Exception e) {
				if (log.isDebugEnabled()) {
					log.debug(LOG_PREFIX + "AbstractWrapper getEntityClass failed wrapperClass={} ",
							wrapper.getClass().getName(), e);
				}
			}
		}
		// 2) Fallback: reflectively call getEntityClass if proxied
		try {
			Method m = wrapper.getClass().getMethod("getEntityClass");
			m.setAccessible(true);
			Object v = m.invoke(wrapper);
			if (v instanceof Class) {
				return (Class<?>) v;
			}
		}
		catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug(LOG_PREFIX + "reflect getEntityClass failed wrapperClass={} ", wrapper.getClass().getName(),
						e);
			}
		}
		// 3) Last resort: reflect protected field entityClass on AbstractWrapper
		try {
			Field f = AbstractWrapper.class.getDeclaredField("entityClass");
			f.setAccessible(true);
			Object v = f.get(wrapper);
			if (v instanceof Class) {
				return (Class<?>) v;
			}
		}
		catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug(LOG_PREFIX + "reflect entityClass field failed wrapperClass={} ",
						wrapper.getClass().getName(), e);
			}
		}
		return null;
	}

	@Getter
	@ToString
	@EqualsAndHashCode
	public static final class ColumnRef {

		private final Class<?> entityClass;

		private final String propertyName;

		private final String columnName;

		public ColumnRef(Class<?> entityClass, String propertyName, String columnName) {
			this.entityClass = entityClass;
			this.propertyName = propertyName;
			this.columnName = columnName;
		}

	}

}
