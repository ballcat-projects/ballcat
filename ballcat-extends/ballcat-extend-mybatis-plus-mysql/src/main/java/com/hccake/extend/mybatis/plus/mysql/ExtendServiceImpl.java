package com.hccake.extend.mybatis.plus.mysql;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collection;

/**
 * 以前继承 com.baomidou.mybatisplus.extension.service.impl.ServiceImpl 的实现类，现在继承本类
 *
 * @author lingting 2020/7/21 10:00
 */
public class ExtendServiceImpl<M extends ExtendBaseMapper<T>, T> extends ServiceImpl<M, T> implements ExtendService<T> {

	@Override
	public int insertByBatch(Collection<T> list) {
		return baseMapper.insertByBatch(list);
	}

	@Override
	public int insertIgnoreByBatch(Collection<T> list) {
		return baseMapper.insertIgnoreByBatch(list);
	}

	@Override
	public int insertOrUpdateByBatch(Collection<T> list, boolean ignore) {
		return baseMapper.insertOrUpdateByBatch(list);
	}

	@Override
	public int insertOrUpdateFieldByBatch(Collection<T> list, Columns<T> columns) {
		return baseMapper.insertOrUpdateFieldByBatch(list, columns);
	}

}
