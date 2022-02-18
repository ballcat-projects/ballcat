package com.hccake.extend.mybatis.plus.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 以前继承 com.baomidou.mybatisplus.extension.service.IService 的实现类，现在继承当前类
 *
 * @author lingting 2020/7/21 9:58
 */
public interface ExtendService<T> {

	// ======= Copy From com.baomidou.mybatisplus.extension.service.IService 开始 =======

	/**
	 * 默认批次提交数量
	 */
	int DEFAULT_BATCH_SIZE = 1000;

	/**
	 * 插入一条记录（选择字段，策略插入）
	 * @param entity 实体对象
	 */
	default boolean save(T entity) {
		return SqlHelper.retBool(getBaseMapper().insert(entity));
	}

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
	default boolean removeById(Serializable id) {
		return SqlHelper.retBool(getBaseMapper().deleteById(id));
	}

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
	default boolean removeById(T entity) {
		return SqlHelper.retBool(getBaseMapper().deleteById(entity));
	}

	/**
	 * 删除（根据ID 批量删除）
	 * @param list 主键ID或实体列表
	 * @return 删除结果
	 */
	default boolean removeByIds(Collection<?> list) {
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		return SqlHelper.retBool(getBaseMapper().deleteBatchIds(list));
	}

	/**
	 * 批量删除
	 * @param list 主键ID或实体列表
	 * @param useFill 是否填充(为true的情况,会将入参转换实体进行delete删除)
	 * @return 删除结果
	 * @since 3.5.0
	 */
	@Transactional(rollbackFor = Exception.class)
	default boolean removeByIds(Collection<?> list, boolean useFill) {
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		if (useFill) {
			return removeBatchByIds(list, true);
		}
		return SqlHelper.retBool(getBaseMapper().deleteBatchIds(list));
	}

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
	default boolean updateById(T entity) {
		return SqlHelper.retBool(getBaseMapper().updateById(entity));
	}

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
	default T getById(Serializable id) {
		return getBaseMapper().selectById(id);
	}

	/**
	 * 查询（根据ID 批量查询）
	 * @param idList 主键ID列表
	 */
	default List<T> listByIds(Collection<? extends Serializable> idList) {
		return getBaseMapper().selectBatchIds(idList);
	}

	/**
	 * 查询所有
	 *
	 */
	default List<T> list() {
		return getBaseMapper().selectList(null);
	}

	/**
	 * 获取对应 entity 的 BaseMapper
	 * @return BaseMapper
	 */
	BaseMapper<T> getBaseMapper();

	/**
	 * 获取 entity 的 class
	 * @return {@link Class<T>}
	 */
	Class<T> getEntityClass();

	// ^^^^^^ Copy From com.baomidou.mybatisplus.extension.service.IService end ^^^^^^

	/**
	 * 批量插入数据
	 * @param list 数据列表
	 * @return int 改动行
	 * @author lingting 2020-08-26 22:11
	 */
	@Transactional(rollbackFor = Exception.class)
	default boolean saveBatchSomeColumn(Collection<T> list) {
		return this.saveBatchSomeColumn(list, DEFAULT_BATCH_SIZE);
	}

	/**
	 * 批量插入数据
	 * @param list 数据列表
	 * @param batchSize 批次插入数据量
	 * @return int 改动行
	 * @author lingting 2020-08-26 22:11
	 */
	boolean saveBatchSomeColumn(Collection<T> list, int batchSize);

}
