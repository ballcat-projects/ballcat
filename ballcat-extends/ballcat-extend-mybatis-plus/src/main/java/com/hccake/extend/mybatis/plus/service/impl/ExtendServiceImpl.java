package com.hccake.extend.mybatis.plus.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.service.ExtendService;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * 以前继承 com.baomidou.mybatisplus.extension.service.impl.ServiceImpl 的实现类，现在继承本类
 *
 * @author lingting 2020/7/21 10:00
 */
@SuppressWarnings("unchecked")
public class ExtendServiceImpl<M extends ExtendMapper<T>, T> implements ExtendService<T> {

	// ======= Copy From com.baomidou.mybatisplus.extension.service.impl.ServiceImpl 开始
	// =======

	protected Log log = LogFactory.getLog(getClass());

	@Autowired
	protected M baseMapper;

	@Override
	public M getBaseMapper() {
		return baseMapper;
	}

	protected Class<T> entityClass = currentModelClass();

	@Override
	public Class<T> getEntityClass() {
		return entityClass;
	}

	protected Class<T> mapperClass = currentMapperClass();

	protected Class<T> currentMapperClass() {
		return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
	}

	protected Class<T> currentModelClass() {
		return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
	}

	/**
	 * 获取mapperStatementId
	 * @param sqlMethod 方法名
	 * @return 命名id
	 * @since 3.4.0
	 */
	protected String getSqlStatement(SqlMethod sqlMethod) {
		return SqlHelper.getSqlStatement(mapperClass, sqlMethod);
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

	// ^^^^^^ Copy From com.baomidou.mybatisplus.extension.service.impl.ServiceImpl end
	// ^^^^^^

	@Override
	public boolean saveBatchSomeColumn(Collection<T> list) {
		if (CollectionUtil.isEmpty(list)) {
			return false;
		}
		int i = baseMapper.insertBatchSomeColumn(list);
		return SqlHelper.retBool(i);
	}

}
