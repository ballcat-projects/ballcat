package com.hccake.extend.mybatis.plus.toolkit;

import com.hccake.extend.mybatis.plus.conditions.query.LambdaAliasQueryWrapperX;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;

/**
 * @author Hccake 2021/1/14
 * @version 1.0
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

}
