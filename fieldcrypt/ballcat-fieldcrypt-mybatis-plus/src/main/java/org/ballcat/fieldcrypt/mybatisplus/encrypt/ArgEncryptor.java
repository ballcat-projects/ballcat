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
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.ballcat.fieldcrypt.core.ClassMetaData;
import org.ballcat.fieldcrypt.core.FieldMetaData;
import org.ballcat.fieldcrypt.core.cache.ClassMetaResolver;
import org.ballcat.fieldcrypt.core.runtime.FieldCryptRuntimeConfig;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;
import org.ballcat.fieldcrypt.mybatisplus.weave.MpWeaveRuntime;

/**
 *
 * 核心参数重写入口：解析列名 -> 查找字段元数据 -> 加密值
 * <p>
 * 该工具类负责在MyBatis-Plus查询构建过程中，对涉及加密字段的查询条件进行自动加密处理。 主要功能包括：
 * <ul>
 * <li>解析SQL查询条件中的列信息（支持SFunction和字符串两种形式）</li>
 * <li>根据列名定位到对应的实体类字段及其加密元数据</li>
 * <li>对查询值进行自动加密处理，确保数据库层面接收到的是加密后的值</li>
 * </ul>
 * 通过此机制，应用层可以像使用普通字段一样使用加密字段，而无需手动处理加解密逻辑。
 * </p>
 *
 * @author Hccake
 * @since 2.0.0
 */
@Slf4j
public final class ArgEncryptor {

	private ArgEncryptor() {
	}

	/**
	 * Compute the new value for the last argument if encryption is applicable; otherwise
	 * return null.
	 */
	public static Object tryRewriteLast(Object wrapper, Object[] args) {
		FieldCryptRuntimeConfig.Snapshot snap = MpWeaveRuntime.snap();
		if (snap == null || !snap.enabled || !snap.enableParameter) {
			return null;
		}
		if (args == null || args.length < 3) {
			return null;
		}

		// 不管是 eq、ne、in、notIn、set，值都在 args[2]，列都是 args[1]
		int valIdx = 2;
		int colIdx = 1; // boolean-leading overloads
		Object columnArg = args[colIdx];
		Object valueArg = args[valIdx];
		if (columnArg == null || valueArg == null) {
			return null;
		}

		ColumnResolver.ColumnRef ref = ColumnResolver.resolve(wrapper, columnArg);
		if (ref == null || ref.entityClass == null) {
			return null;
		}

		ClassMetaResolver resolver = MpWeaveRuntime.resolver();
		if (resolver == null) {
			return null;
		}
		ClassMetaData meta = resolver.resolve(ref.entityClass);
		if (meta == null || !meta.shouldProcess()) {
			return null;
		}

		FieldMetaData fmeta = locateField(meta, ref.propertyName, ref.columnName);
		if (fmeta == null) {
			return null;
		}

		CryptoEngine crypto = MpWeaveRuntime.crypto();
		if (crypto == null) {
			return null;
		}

		return ValueEncryptor.encryptValue(valueArg, fmeta, crypto);
	}

	private static FieldMetaData locateField(ClassMetaData meta, String propertyName, String columnName) {
		if (meta == null) {
			return null;
		}
		if (propertyName != null && !propertyName.isEmpty()) {
			for (FieldMetaData fm : meta.getEncryptedFields()) {
				if (fm.getField().getName().equals(propertyName)) {
					return fm;
				}
			}
		}
		if (columnName != null && !columnName.isEmpty()) {
			for (FieldMetaData fm : meta.getEncryptedFields()) {
				String fn = fm.getField().getName();
				if (columnName.equalsIgnoreCase(fn) || columnName.equalsIgnoreCase(toSnake(fn))) {
					return fm;
				}
			}
		}
		return null;
	}

	static String toSnake(String camel) {
		if (camel == null || camel.isEmpty()) {
			return camel;
		}
		StringBuilder sb = new StringBuilder(camel.length() + 8);
		for (int i = 0; i < camel.length(); i++) {
			char c = camel.charAt(i);
			if (Character.isUpperCase(c)) {
				if (i > 0) {
					sb.append('_');
				}
				sb.append(Character.toLowerCase(c));
			}
			else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Resolve column argument to entity class and property/column name using MyBatis-Plus
	 * utilities.
	 */
	static final class ColumnResolver {

		private ColumnResolver() {
		}

		static ColumnRef resolve(Object wrapper, Object columnArg) {
			if (columnArg == null) {
				return null;
			}

			// SFunction path: use LambdaUtils + ColumnCache
			if (columnArg instanceof SFunction) {
				SFunction<?, ?> fn = (SFunction<?, ?>) columnArg;

				LambdaMeta meta = LambdaUtils.extract(fn);
				String property = PropertyNamer.methodToProperty(meta.getImplMethodName());
				Class<?> entityClass = extractEntityClassFromWrapper(wrapper);
				if (entityClass == null) {
					entityClass = deriveEntityClassFromLambda(fn);
				}

				String column = null;
				if (entityClass != null) {
					Map<String, ColumnCache> cache = LambdaUtils.getColumnMap(entityClass);
					if (cache != null) {
						ColumnCache cc = cache.get(property);
						if (cc != null) {
							column = cc.getColumn();
						}
					}
					if (column == null) {
						column = findColumnViaTableInfo(entityClass, property);
					}
				}
				return new ColumnRef(entityClass, property, column);
			}

			// String column path: respect provided column, entity for downstream lookup
			if (columnArg instanceof CharSequence) {
				String column = columnArg.toString();
				Class<?> entityClass = extractEntityClassFromWrapper(wrapper);
				TableInfo ti = TableInfoHelper.getTableInfo(entityClass);

				String property = null;
				if (ti != null) {
					try {
						if (column.equalsIgnoreCase(ti.getKeyColumn())) {
							property = ti.getKeyProperty();
						}
						else {
							for (TableFieldInfo fi : ti.getFieldList()) {
								if (column.equalsIgnoreCase(fi.getColumn())) {
									property = fi.getProperty();
									break;
								}
							}
						}
					}
					catch (Throwable ignored) {
					}
				}
				return new ColumnRef(entityClass, property, column);
			}
			return null;
		}

		private static Class<?> deriveEntityClassFromLambda(SFunction<?, ?> fn) {
			try {
				Object sl = LambdaUtils.extract(fn); // MP SerializedLambda
				// Prefer getImplClassName() if present
				try {
					Method m = sl.getClass().getMethod("getImplClassName");
					Object v = m.invoke(sl);
					if (v != null) {
						return forNameQuiet(v.toString());
					}
				}
				catch (NoSuchMethodException ignored) {
				}
				// Fallback: getImplClass() returning internal name with '/'
				try {
					Method m = sl.getClass().getMethod("getImplClass");
					Object v = m.invoke(sl);
					if (v != null) {
						String n = v.toString().replace('/', '.');
						return forNameQuiet(n);
					}
				}
				catch (NoSuchMethodException ignored) {
				}
			}
			catch (Throwable e) {
				if (log.isDebugEnabled()) {
					log.debug("FieldCrypt | mp | deriveEntityClassFromLambda failed", e);
				}
			}
			return null;
		}

		private static String findColumnViaTableInfo(Class<?> entityClass, String property) {
			try {
				TableInfo ti = TableInfoHelper.getTableInfo(entityClass);
				if (ti != null) {
					for (TableFieldInfo fi : ti.getFieldList()) {
						if (fi.getProperty().equals(property)) {
							return fi.getColumn();
						}
					}
					// also check keyProperty
					if (property.equals(ti.getKeyProperty())) {
						return ti.getKeyColumn();
					}
				}
			}
			catch (Throwable e) {
				if (log.isDebugEnabled()) {
					log.debug(
							"FieldCrypt | mp | TableInfoHelper.getTableInfo or resolve column failed entityClass={} property={}",
							entityClass, property, e);
				}
			}
			return null;
		}

		private static Class<?> forNameQuiet(String name) {
			try {
				return Class.forName(name);
			}
			catch (Throwable e) {
				if (log.isDebugEnabled()) {
					log.debug("FieldCrypt | mp | forNameQuiet failed class={} ", name, e);
				}
				return null;
			}
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
				catch (Throwable e) {
					if (log.isDebugEnabled()) {
						log.debug("FieldCrypt | mp | AbstractWrapper getEntityClass failed wrapperClass={} ",
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
			catch (Throwable e) {
				if (log.isDebugEnabled()) {
					log.debug("FieldCrypt | mp | reflect getEntityClass failed wrapperClass={} ",
							wrapper.getClass().getName(), e);
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
			catch (Throwable e) {
				if (log.isDebugEnabled()) {
					log.debug("FieldCrypt | mp | reflect entityClass field failed wrapperClass={} ",
							wrapper.getClass().getName(), e);
				}
			}
			return null;
		}

		static final class ColumnRef {

			final Class<?> entityClass;

			final String propertyName;

			final String columnName;

			ColumnRef(Class<?> entityClass, String propertyName, String columnName) {
				this.entityClass = entityClass;
				this.propertyName = propertyName;
				this.columnName = columnName;
			}

		}

	}

}
