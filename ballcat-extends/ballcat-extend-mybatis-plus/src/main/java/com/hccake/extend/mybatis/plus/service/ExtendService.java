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
	 * 根据 ID 删除
	 * @param id 主键ID
	 */
	default boolean removeById(Serializable id) {
		return SqlHelper.retBool(getBaseMapper().deleteById(id));
	}

	/**
	 * 删除（根据ID 批量删除）
	 * @param idList 主键ID列表
	 */
	default boolean removeByIds(Collection<? extends Serializable> idList) {
		if (CollectionUtils.isEmpty(idList)) {
			return false;
		}
		return SqlHelper.retBool(getBaseMapper().deleteBatchIds(idList));
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
	boolean saveBatchSomeColumn(Collection<T> list);

}
