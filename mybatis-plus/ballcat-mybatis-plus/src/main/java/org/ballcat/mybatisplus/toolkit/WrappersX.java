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

package org.ballcat.mybatisplus.toolkit;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.ballcat.mybatisplus.conditions.query.LambdaAliasQueryWrapperX;
import org.ballcat.mybatisplus.conditions.query.LambdaQueryWrapperX;

/**
 * @author Hccake 2021/1/14
 *
 */
public final class WrappersX {

	private WrappersX() {
	}

	/**
	 * 获取 LambdaQueryWrapperX&lt;T&gt;
	 * @param <T> 实体类泛型
	 * @return LambdaQueryWrapperX&lt;T&gt;
	 */
	public static <T> LambdaQueryWrapperX<T> lambdaQueryX() {
		return new LambdaQueryWrapperX<>();
	}

	/**
	 * 获取 LambdaQueryWrapperX&lt;T&gt;
	 * @param entity 实体类
	 * @param <T> 实体类泛型
	 * @return LambdaQueryWrapperX&lt;T&gt;
	 */
	public static <T> LambdaQueryWrapperX<T> lambdaQueryX(T entity) {
		return new LambdaQueryWrapperX<>(entity);
	}

	/**
	 * 获取 LambdaQueryWrapperX&lt;T&gt;
	 * @param entityClass 实体类class
	 * @param <T> 实体类泛型
	 * @return LambdaQueryWrapperX&lt;T&gt;
	 * @since 3.3.1
	 */
	public static <T> LambdaQueryWrapperX<T> lambdaQueryX(Class<T> entityClass) {
		return new LambdaQueryWrapperX<>(entityClass);
	}

	/**
	 * 获取 LambdaAliasQueryWrapper&lt;T&gt;
	 * @param entity 实体类
	 * @param <T> 实体类泛型
	 * @return LambdaAliasQueryWrapper&lt;T&gt;
	 */
	public static <T> LambdaAliasQueryWrapperX<T> lambdaAliasQueryX(T entity) {
		return new LambdaAliasQueryWrapperX<>(entity);
	}

	/**
	 * 获取 LambdaAliasQueryWrapper&lt;T&gt;
	 * @param entityClass 实体类class
	 * @param <T> 实体类泛型
	 * @return LambdaAliasQueryWrapper&lt;T&gt;
	 * @since 3.3.1
	 */
	public static <T> LambdaAliasQueryWrapperX<T> lambdaAliasQueryX(Class<T> entityClass) {
		return new LambdaAliasQueryWrapperX<>(entityClass);
	}

	/**
	 * 获取 LambdaUpdateWrapper&lt;T&gt; 复制 com.baomidou.mybatisplus.core.toolkit.Wrappers
	 * @param entity 实体类
	 * @param <T> 实体类泛型
	 * @return LambdaUpdateWrapper&lt;T&gt;
	 */
	public static <T> LambdaUpdateWrapper<T> lambdaUpdate(T entity) {
		return new LambdaUpdateWrapper<>(entity);
	}

	/**
	 * 获取 LambdaUpdateWrapper&lt;T&gt; 复制 com.baomidou.mybatisplus.core.toolkit.Wrappers
	 * @param entityClass 实体类class
	 * @param <T> 实体类泛型
	 * @return LambdaUpdateWrapper&lt;T&gt;
	 * @since 3.3.1
	 */
	public static <T> LambdaUpdateWrapper<T> lambdaUpdate(Class<T> entityClass) {
		return new LambdaUpdateWrapper<>(entityClass);
	}

}
