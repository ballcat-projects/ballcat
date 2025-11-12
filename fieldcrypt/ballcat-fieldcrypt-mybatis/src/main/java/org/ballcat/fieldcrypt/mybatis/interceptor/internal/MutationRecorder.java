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

package org.ballcat.fieldcrypt.mybatis.interceptor.internal;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 记录本次加密修改的原始值，供执行后回滚使用。 仅在 restorePlaintext=true 时有效。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class MutationRecorder {

	private final List<Mutation> mutations = new ArrayList<>();

	/**
	 * 记录一个变更。
	 */
	public void record(Mutation mutation) {
		this.mutations.add(mutation);
	}

	public boolean isEmpty() {
		return this.mutations.isEmpty();
	}

	public void restoreAll() {
		// 逆序恢复，避免潜在依赖
		for (int i = this.mutations.size() - 1; i >= 0; i--) {
			this.mutations.get(i).restore();
		}
	}

	// ========== 工厂方法 ==========

	/**
	 * 创建字段变更记录。
	 */
	public static Mutation fieldMutation(Object target, Field field, String original) {
		return new FieldMutation(target, field, original);
	}

	/**
	 * 创建 Map 条目变更记录。
	 */
	public static Mutation mapEntryMutation(Map<Object, Object> map, Object key, String original) {
		return new MapEntryMutation(map, key, original);
	}

	/**
	 * 创建 List 索引变更记录。
	 */
	public static Mutation listIndexMutation(List<Object> list, int index, String original) {
		return new ListIndexMutation(list, index, original);
	}

	/**
	 * 创建数组索引变更记录。
	 */
	public static Mutation arrayIndexMutation(Object array, int index, String original) {
		return new ArrayIndexMutation(array, index, original);
	}

	/**
	 * 创建 Collection 批量替换变更记录（用于 Set、Queue 等不支持索引的集合）。
	 */
	public static Mutation collectionReplaceMutation(Collection<Object> collection, List<Object> originalElements) {
		return new CollectionReplaceMutation(collection, originalElements);
	}

	// ========== Mutation ==========

	/**
	 * 统一的 Mutation 接口。
	 */
	public interface Mutation {

		void restore();

	}

	/**
	 * 字段变更。
	 */
	private static final class FieldMutation implements Mutation {

		private final Object target;

		private final Field field;

		private final String original;

		FieldMutation(Object target, Field field, String original) {
			this.target = target;
			this.field = field;
			this.original = original;
		}

		@Override
		public void restore() {
			try {
				this.field.set(this.target, this.original);
			}
			catch (IllegalAccessException ignored) {
			}
		}

	}

	/**
	 * Map 条目变更。
	 */
	private static final class MapEntryMutation implements Mutation {

		private final Map<Object, Object> map;

		private final Object key;

		private final String original;

		MapEntryMutation(Map<Object, Object> map, Object key, String original) {
			this.map = map;
			this.key = key;
			this.original = original;
		}

		@Override
		public void restore() {
			this.map.put(this.key, this.original);
		}

	}

	/**
	 * List 索引变更。
	 */
	private static final class ListIndexMutation implements Mutation {

		private final List<Object> list;

		private final int index;

		private final String original;

		ListIndexMutation(List<Object> list, int index, String original) {
			this.list = list;
			this.index = index;
			this.original = original;
		}

		@Override
		public void restore() {
			if (this.index >= 0 && this.index < this.list.size()) {
				this.list.set(this.index, this.original);
			}
		}

	}

	/**
	 * 数组索引变更。
	 */
	private static final class ArrayIndexMutation implements Mutation {

		private final Object array;

		private final int index;

		private final String original;

		ArrayIndexMutation(Object array, int index, String original) {
			this.array = array;
			this.index = index;
			this.original = original;
		}

		@Override
		public void restore() {
			Array.set(this.array, this.index, this.original);
		}

	}

	/**
	 * Collection 批量替换变更（用于 Set、Queue 等不支持索引的集合）。
	 */
	private static final class CollectionReplaceMutation implements Mutation {

		private final Collection<Object> collection;

		private final List<Object> originalElements;

		CollectionReplaceMutation(Collection<Object> collection, List<Object> originalElements) {
			this.collection = collection;
			// 防御性拷贝，避免外部修改影响回滚
			this.originalElements = new ArrayList<>(originalElements);
		}

		@Override
		public void restore() {
			this.collection.clear();
			this.collection.addAll(this.originalElements);
		}

	}

}
