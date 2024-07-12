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

package org.ballcat.common.model.domain;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 树结构函数包装类.
 *
 * @param <T> 树节点类型
 * @param <I> 节点 key 类型
 * @author Hccake
 * @since 2.0.0
 */
public class TreeFunctionWrapper<T, I> {

	/**
	 * 节点 key 提取器。
	 */
	private final Function<T, I> keyExtractor;

	/**
	 * 父节点 key 提取器。
	 */
	private final Function<T, I> parentKeyExtractor;

	/**
	 * 子节点设置器。
	 */
	private final BiConsumer<T, List<T>> childrenSetter;

	/**
	 * 子节点获取器。
	 */
	private final Function<T, List<T>> childrenGetter;

	public TreeFunctionWrapper(Function<T, I> keyExtractor, Function<T, I> parentKeyExtractor,
			BiConsumer<T, List<T>> childrenSetter, Function<T, List<T>> childrenGetter) {
		this.keyExtractor = keyExtractor;
		this.parentKeyExtractor = parentKeyExtractor;
		this.childrenSetter = childrenSetter;
		this.childrenGetter = childrenGetter;
	}

	public Function<T, I> keyExtractor() {
		return this.keyExtractor;
	}

	public Function<T, I> parentKeyExtractor() {
		return this.parentKeyExtractor;
	}

	public BiConsumer<T, List<T>> childrenSetter() {
		return this.childrenSetter;
	}

	public Function<T, List<T>> childrenGetter() {
		return this.childrenGetter;
	}

}
