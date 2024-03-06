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

package org.ballcat.mybatisplus.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.ballcat.mybatisplus.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 对 mybatis plus 提供的 IService 服务类做了精简，以防止在 mapper 外使用 Wrapper 构建 Sql
 *
 * @author Hccake
 * @since 2.0.0
 */
@SuppressWarnings("unchecked")
public class BaseServiceImpl<M extends BaseMapper<T>, T> implements BaseService<T> {

	protected Log log = LogFactory.getLog(getClass());

	@Autowired
	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	protected M baseMapper;

	protected Class<T> entityClass = currentModelClass();

	@Override
	public Class<T> getEntityClass() {
		return this.entityClass;
	}

	protected Class<M> mapperClass = currentMapperClass();

	protected Class<M> currentMapperClass() {
		return (Class<M>) ReflectionKit.getSuperClassGenericType(this.getClass(), ExtendServiceImpl.class, 0);
	}

	protected Class<T> currentModelClass() {
		return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), ExtendServiceImpl.class, 1);
	}

	/**
	 * 插入一条记录（选择字段，策略插入）
	 * @param entity 实体对象
	 */
	@Override
	public boolean save(T entity) {
		return SqlHelper.retBool(this.baseMapper.insert(entity));
	}

	/**
	 * 批量插入
	 * @param entityList ignore
	 * @param batchSize ignore
	 * @return ignore
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean saveBatch(Collection<T> entityList, int batchSize) {
		String sqlStatement = getSqlStatement(SqlMethod.INSERT_ONE);
		return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
	}

	/**
	 * 获取mapperStatementId
	 * @param sqlMethod 方法名
	 * @return 命名id
	 * @since 3.4.0
	 */
	protected String getSqlStatement(SqlMethod sqlMethod) {
		return SqlHelper.getSqlStatement(this.mapperClass, sqlMethod);
	}

	/**
	 * TableId 注解存在更新记录，否插入一条记录
	 * @param entity 实体对象
	 * @return boolean
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean saveOrUpdate(T entity) {
		if (null != entity) {
			TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
			Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
			String keyProperty = tableInfo.getKeyProperty();
			Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
			Object idVal = tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());
			return StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal)) ? save(entity)
					: updateById(entity);
		}
		return false;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
		TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
		Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
		String keyProperty = tableInfo.getKeyProperty();
		Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
		return SqlHelper.saveOrUpdateBatch(this.entityClass, this.mapperClass, this.log, entityList, batchSize,
				(sqlSession, entity) -> {
					Object idVal = tableInfo.getPropertyValue(entity, keyProperty);
					return StringUtils.checkValNull(idVal) || CollectionUtils
						.isEmpty(sqlSession.selectList(getSqlStatement(SqlMethod.SELECT_BY_ID), entity));
				}, (sqlSession, entity) -> {
					MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
					param.put(Constants.ENTITY, entity);
					sqlSession.update(getSqlStatement(SqlMethod.UPDATE_BY_ID), param);
				});
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean updateBatchById(Collection<T> entityList, int batchSize) {
		String sqlStatement = getSqlStatement(SqlMethod.UPDATE_BY_ID);
		return executeBatch(entityList, batchSize, (sqlSession, entity) -> {
			MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
			param.put(Constants.ENTITY, entity);
			sqlSession.update(sqlStatement, param);
		});
	}

	/**
	 * 根据 ID 选择修改
	 * @param entity 实体对象
	 */
	@Override
	public boolean updateById(T entity) {
		return SqlHelper.retBool(this.baseMapper.updateById(entity));
	}

	/**
	 * 执行批量操作
	 * @param list 数据集合
	 * @param batchSize 批量大小
	 * @param consumer 执行方法
	 * @param <E> 泛型
	 * @return 操作结果
	 * @since 3.3.1
	 */
	protected <E> boolean executeBatch(Collection<E> list, int batchSize, BiConsumer<SqlSession, E> consumer) {
		return SqlHelper.executeBatch(this.entityClass, this.log, list, batchSize, consumer);
	}

	@Override
	public boolean removeById(Serializable id) {
		TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityClass());
		if (tableInfo.isWithLogicDelete() && tableInfo.isWithUpdateFill()) {
			return removeById(id, true);
		}
		return SqlHelper.retBool(this.baseMapper.deleteById(id));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeByIds(Collection<?> list) {
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityClass());
		if (tableInfo.isWithLogicDelete() && tableInfo.isWithUpdateFill()) {
			return removeBatchByIds(list, true);
		}
		return SqlHelper.retBool(this.baseMapper.deleteBatchIds(list));
	}

	@Override
	public boolean removeById(Serializable id, boolean useFill) {
		TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
		if (useFill && tableInfo.isWithLogicDelete()) {
			if (this.entityClass.isAssignableFrom(id.getClass())) {
				return SqlHelper.retBool(this.baseMapper.deleteById(id));
			}
			T instance = tableInfo.newInstance();
			tableInfo.setPropertyValue(instance, tableInfo.getKeyProperty(), id);
			return removeById(instance);
		}
		return SqlHelper.retBool(this.baseMapper.deleteById(id));
	}

	/**
	 * 根据实体(ID)删除
	 * @param entity 实体
	 * @return 删除结果
	 * @since 3.4.4
	 */
	public boolean removeById(T entity) {
		return SqlHelper.retBool(this.baseMapper.deleteById(entity));
	}

	/**
	 * 批量删除
	 * @param list 主键ID或实体列表
	 * @param useFill 是否填充(为true的情况,会将入参转换实体进行delete删除)
	 * @return 删除结果
	 * @since 3.5.0
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeByIds(Collection<?> list, boolean useFill) {
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		if (useFill) {
			return removeBatchByIds(list, true);
		}
		return SqlHelper.retBool(this.baseMapper.deleteBatchIds(list));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeBatchByIds(Collection<?> list, int batchSize) {
		TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
		return removeBatchByIds(list, batchSize, tableInfo.isWithLogicDelete() && tableInfo.isWithUpdateFill());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeBatchByIds(Collection<?> list, int batchSize, boolean useFill) {
		String sqlStatement = getSqlStatement(SqlMethod.DELETE_BY_ID);
		TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
		return executeBatch(list, batchSize, (sqlSession, e) -> {
			if (useFill && tableInfo.isWithLogicDelete()) {
				if (this.entityClass.isAssignableFrom(e.getClass())) {
					sqlSession.update(sqlStatement, e);
				}
				else {
					T instance = tableInfo.newInstance();
					tableInfo.setPropertyValue(instance, tableInfo.getKeyProperty(), e);
					sqlSession.update(sqlStatement, instance);
				}
			}
			else {
				sqlSession.update(sqlStatement, e);
			}
		});
	}

	/**
	 * 根据 ID 查询
	 * @param id 主键ID
	 */
	public T getById(Serializable id) {
		return this.baseMapper.selectById(id);
	}

	/**
	 * 查询（根据ID 批量查询）
	 * @param idList 主键ID列表
	 */
	public List<T> listByIds(Collection<? extends Serializable> idList) {
		return this.baseMapper.selectBatchIds(idList);
	}

	/**
	 * 查询所有
	 */
	public List<T> list() {
		return this.baseMapper.selectList(null);
	}

}
