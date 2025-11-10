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

import lombok.extern.slf4j.Slf4j;
import org.ballcat.fieldcrypt.core.ClassMetaData;
import org.ballcat.fieldcrypt.core.FieldMetaData;
import org.ballcat.fieldcrypt.core.cache.ClassMetaResolver;
import org.ballcat.fieldcrypt.core.runtime.FieldCryptRuntimeConfig;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;
import org.ballcat.fieldcrypt.mybatisplus.weave.MpWeaveRuntime;

/**
 *
 * 核心参数重写入口：解析列名 -> 查找字段元数据 -> 加密值.
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
	 * MyBatis-Plus wrapper 参数位置常量
	 */
	private static final int ARG_INDEX_COLUMN = 1;

	private static final int ARG_INDEX_VALUE = 2;

	private static final int MIN_ARGS_LENGTH = 3;

	/**
	 * 当满足运行时加密开关且目标字段为加密字段时，计算最后一个参数（值参数）的加密后结果；否则返回 null 表示不改写。
	 * <p>
	 * 输入/输出约定：
	 * <ul>
	 * <li>入参：wrapper（查询/更新构造器），args（至少包含列与值，期望下标1为列、2为值）</li>
	 * <li>返回：加密后的新值对象；若不需要/无法加密则返回 null，调用方据此决定是否替换</li>
	 * </ul>
	 * 注意：返回 null 同时用于“不处理”的语义，调用方不应将其视为“值即为 null 的合法替换”。
	 */
	public static Object tryRewriteLast(Object wrapper, Object[] args) {
		FieldCryptRuntimeConfig.Snapshot snap = MpWeaveRuntime.snap();
		if (!parameterEncryptionEnabled(snap)) {
			return null;
		}

		// 不管是 eq、ne、in、notIn、set，值都在 args[2]，列都是 args[1]
		ArgPair pair = extractColumnAndValue(args);
		if (pair == null) {
			return null;
		}
		Object columnArg = pair.columnArg;
		Object valueArg = pair.valueArg;

		ColumnResolver.ColumnRef ref = ColumnResolver.resolve(wrapper, columnArg);
		if (ref == null || ref.getEntityClass() == null) {
			return null;
		}

		ClassMetaResolver resolver = MpWeaveRuntime.resolver();
		if (resolver == null) {
			return null;
		}
		ClassMetaData meta = resolver.resolve(ref.getEntityClass());
		if (meta == null || !meta.shouldProcess()) {
			return null;
		}

		FieldMetaData fmeta = locateField(meta, ref.getPropertyName());
		if (fmeta == null) {
			return null;
		}

		CryptoEngine crypto = MpWeaveRuntime.crypto();
		if (crypto == null) {
			return null;
		}

		return ValueEncryptor.encryptValue(valueArg, fmeta, crypto);
	}

	/**
	 * 参数开关是否开启（快照存在且启用参数加密）。
	 */
	private static boolean parameterEncryptionEnabled(FieldCryptRuntimeConfig.Snapshot snap) {
		return snap != null && snap.enabled && snap.enableParameter;
	}

	/**
	 * 从入参中提取列和值，如果参数不足或为 null 则返回 null
	 */
	private static ArgPair extractColumnAndValue(Object[] args) {
		if (args == null || args.length < MIN_ARGS_LENGTH) {
			return null;
		}
		Object columnArg = args[ARG_INDEX_COLUMN];
		Object valueArg = args[ARG_INDEX_VALUE];
		if (columnArg == null || valueArg == null) {
			return null;
		}
		return new ArgPair(columnArg, valueArg);
	}

	/**
	 * 基于类元数据与属性名定位加密字段元信息，仅按 propertyName 精确匹配。
	 */
	private static FieldMetaData locateField(ClassMetaData meta, String propertyName) {
		if (meta == null || propertyName == null || propertyName.isEmpty()) {
			return null;
		}
		return meta.getEncryptedFieldMap().get(propertyName);
	}

	/**
	 * 简单的二元组用于承载列和值。
	 */
	private static final class ArgPair {

		final Object columnArg;

		final Object valueArg;

		ArgPair(Object columnArg, Object valueArg) {
			this.columnArg = columnArg;
			this.valueArg = valueArg;
		}

	}

}
