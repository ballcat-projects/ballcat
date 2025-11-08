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

package org.ballcat.fieldcrypt.mybatis.internal;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 记录本次加密修改的原始值，供执行后回滚使用。 仅在 restorePlaintext=true 时有效。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class MutationRecorder {

	private final List<FieldMutation> fieldMutations = new ArrayList<>();

	private final List<MapEntryMutation> mapMutations = new ArrayList<>();

	private final List<DirectParamMutation> directMutations = new ArrayList<>();

	private final List<ListIndexMutation> listMutations = new ArrayList<>();

	private final List<ArrayIndexMutation> arrayMutations = new ArrayList<>();

	public void recordField(Object target, Field field, String original) {
		this.fieldMutations.add(new FieldMutation(target, field, original));
	}

	public void recordMapEntry(java.util.Map<Object, Object> map, Object key, String original) {
		this.mapMutations.add(new MapEntryMutation(map, key, original));
	}

	public void recordDirect(DirectParamMutation m) {
		this.directMutations.add(m);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void recordListIndex(List<?> list, int index, String original) {
		this.listMutations.add(new ListIndexMutation((List) list, index, original));
	}

	public void recordArrayIndex(Object array, int index, String original) {
		this.arrayMutations.add(new ArrayIndexMutation(array, index, original));
	}

	public boolean isEmpty() {
		return this.fieldMutations.isEmpty() && this.mapMutations.isEmpty() && this.directMutations.isEmpty()
				&& this.listMutations.isEmpty() && this.arrayMutations.isEmpty();
	}

	public void restoreAll() {
		// 逆序恢复，避免潜在依赖（一般不需要，但保险）
		for (int i = this.directMutations.size() - 1; i >= 0; i--) {
			DirectParamMutation m = this.directMutations.get(i);
			m.restore();
		}
		for (int i = this.listMutations.size() - 1; i >= 0; i--) {
			ListIndexMutation m = this.listMutations.get(i);
			m.restore();
		}
		for (int i = this.arrayMutations.size() - 1; i >= 0; i--) {
			ArrayIndexMutation m = this.arrayMutations.get(i);
			m.restore();
		}
		for (int i = this.mapMutations.size() - 1; i >= 0; i--) {
			MapEntryMutation m = this.mapMutations.get(i);
			m.restore();
		}
		for (int i = this.fieldMutations.size() - 1; i >= 0; i--) {
			FieldMutation m = this.fieldMutations.get(i);
			m.restore();
		}
	}

	public static class FieldMutation {

		private final Object target;

		private final Field field;

		private final String original;

		public FieldMutation(Object target, Field field, String original) {
			this.target = target;
			this.field = field;
			this.original = original;
		}

		public void restore() {
			try {
				this.field.set(this.target, this.original);
			}
			catch (IllegalAccessException ignored) {
			}
		}

	}

	public static class MapEntryMutation {

		private final java.util.Map<Object, Object> map;

		private final Object key;

		private final String original;

		public MapEntryMutation(java.util.Map<Object, Object> map, Object key, String original) {
			this.map = map;
			this.key = key;
			this.original = original;
		}

		public void restore() {
			this.map.put(this.key, this.original);
		}

	}

	/**
	 * 直接参数（如 ParamMap 中的 String）恢复包装。
	 */
	public static class DirectParamMutation {

		private final java.util.Map<String, Object> paramMap; // 可为 null（代表单值容器）

		private final String key; // paramMap key

		private final Holder holder; // 单值容器

		private final String original;

		public DirectParamMutation(java.util.Map<String, Object> paramMap, String key, Holder holder, String original) {
			this.paramMap = paramMap;
			this.key = key;
			this.holder = holder;
			this.original = original;
		}

		public void restore() {
			if (this.paramMap != null && this.key != null) {
				this.paramMap.put(this.key, this.original);
			}
			else if (this.holder != null) {
				this.holder.value = this.original;
			}
		}

	}

	/** 简单可变引用包装 */
	public static class Holder {

		public String value;

	}

	public static class ListIndexMutation {

		private final List<Object> list;

		private final int index;

		private final String original;

		public ListIndexMutation(List<Object> list, int index, String original) {
			this.list = list;
			this.index = index;
			this.original = original;
		}

		public void restore() {
			if (this.index >= 0 && this.index < this.list.size()) {
				this.list.set(this.index, this.original);
			}
		}

	}

	public static class ArrayIndexMutation {

		private final Object array;

		private final int index;

		private final String original;

		public ArrayIndexMutation(Object array, int index, String original) {
			this.array = array;
			this.index = index;
			this.original = original;
		}

		public void restore() {
			try {
				Array.set(this.array, this.index, this.original);
			}
			catch (Exception ignored) {
				// 忽略
			}
		}

	}

}
