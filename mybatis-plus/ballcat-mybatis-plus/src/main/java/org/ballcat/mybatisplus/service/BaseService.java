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

package org.ballcat.mybatisplus.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * 对 mybatis plus 提供的 IService 服务类做了精简，以防止在 mapper 外使用 Wrapper 构建 Sql
 *
 * @author Hccake
 * @since 2.0.0
 */
public interface BaseService<T> {

	/**
	 * 默认批次提交数量
	 */
	int DEFAULT_BATCH_SIZE = 1000;

	/**
	 * 插入一条记录（选择字段，策略插入）
	 * @param entity 实体对象
	 */
	boolean save(T entity);

	/**
	 * 插入（批量）
	 * @param entityList 实体对象集合
	 */
	@Transactional(rollbackFor = Exception.class)
	default boolean saveBatch(Collection<T> entityList) {
		return saveBatch(entityList, DEFAULT_BATCH_SIZE);
	}

	/**
	 * 插入（批量）
	 * @param entityList 实体对象集合
	 * @param batchSize 插入批次数量
	 */
	boolean saveBatch(Collection<T> entityList, int batchSize);

	/**
	 * 批量修改插入
	 * @param entityList 实体对象集合
	 */
	@Transactional(rollbackFor = Exception.class)
	default boolean saveOrUpdateBatch(Collection<T> entityList) {
		return saveOrUpdateBatch(entityList, DEFAULT_BATCH_SIZE);
	}

	/**
	 * 批量修改插入
	 * @param entityList 实体对象集合
	 * @param batchSize 每次的数量
	 */
	boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize);

	/**
	 * 根据 ID 删除
	 * @param id 主键ID
	 */
	boolean removeById(Serializable id);

	/**
	 * 根据 ID 删除
	 * @param id 主键(类型必须与实体类型字段保持一致)
	 * @param useFill 是否启用填充(为true的情况,会将入参转换实体进行delete删除)
	 * @return 删除结果
	 * @since 3.5.0
	 */
	default boolean removeById(Serializable id, boolean useFill) {
		throw new UnsupportedOperationException("不支持的方法!");
	}

	/**
	 * 根据实体(ID)删除
	 * @param entity 实体
	 * @since 3.4.4
	 * @return 删除结果
	 */
	boolean removeById(T entity);

	/**
	 * 删除（根据ID 批量删除）
	 * @param list 主键ID或实体列表
	 * @return 删除结果
	 */
	boolean removeByIds(Collection<?> list);

	/**
	 * 批量删除
	 * @param list 主键ID或实体列表
	 * @param useFill 是否填充(为true的情况,会将入参转换实体进行delete删除)
	 * @return 删除结果
	 * @since 3.5.0
	 */
	boolean removeByIds(Collection<?> list, boolean useFill);

	/**
	 * 批量删除(jdbc批量提交)
	 * @param list 主键ID或实体列表(主键ID类型必须与实体类型字段保持一致)
	 * @return 删除结果
	 * @since 3.5.0
	 */
	@Transactional(rollbackFor = Exception.class)
	default boolean removeBatchByIds(Collection<?> list) {
		return removeBatchByIds(list, DEFAULT_BATCH_SIZE);
	}

	/**
	 * 批量删除(jdbc批量提交)
	 * @param list 主键ID或实体列表(主键ID类型必须与实体类型字段保持一致)
	 * @param useFill 是否启用填充(为true的情况,会将入参转换实体进行delete删除)
	 * @return 删除结果
	 * @since 3.5.0
	 */
	@Transactional(rollbackFor = Exception.class)
	default boolean removeBatchByIds(Collection<?> list, boolean useFill) {
		return removeBatchByIds(list, DEFAULT_BATCH_SIZE, useFill);
	}

	/**
	 * 批量删除(jdbc批量提交)
	 * @param list 主键ID或实体列表
	 * @param batchSize 批次大小
	 * @return 删除结果
	 * @since 3.5.0
	 */
	default boolean removeBatchByIds(Collection<?> list, int batchSize) {
		throw new UnsupportedOperationException("不支持的方法!");
	}

	/**
	 * 批量删除(jdbc批量提交)
	 * @param list 主键ID或实体列表
	 * @param batchSize 批次大小
	 * @param useFill 是否启用填充(为true的情况,会将入参转换实体进行delete删除)
	 * @return 删除结果
	 * @since 3.5.0
	 */
	default boolean removeBatchByIds(Collection<?> list, int batchSize, boolean useFill) {
		throw new UnsupportedOperationException("不支持的方法!");
	}

	/**
	 * 根据 ID 选择修改
	 * @param entity 实体对象
	 */
	boolean updateById(T entity);

	/**
	 * 根据ID 批量更新
	 * @param entityList 实体对象集合
	 */
	@Transactional(rollbackFor = Exception.class)
	default boolean updateBatchById(Collection<T> entityList) {
		return updateBatchById(entityList, DEFAULT_BATCH_SIZE);
	}

	/**
	 * 根据ID 批量更新
	 * @param entityList 实体对象集合
	 * @param batchSize 更新批次数量
	 */
	boolean updateBatchById(Collection<T> entityList, int batchSize);

	/**
	 * TableId 注解存在更新记录，否插入一条记录
	 * @param entity 实体对象
	 */
	boolean saveOrUpdate(T entity);

	/**
	 * 根据 ID 查询
	 * @param id 主键ID
	 */
	T getById(Serializable id);

	/**
	 * 查询（根据ID 批量查询）
	 * @param idList 主键ID列表
	 */
	List<T> listByIds(Collection<? extends Serializable> idList);

	/**
	 * 查询所有
	 *
	 */
	List<T> list();

	/**
	 * 获取 entity 的 class
	 * @return {@link Class<T>}
	 */
	Class<T> getEntityClass();

}
