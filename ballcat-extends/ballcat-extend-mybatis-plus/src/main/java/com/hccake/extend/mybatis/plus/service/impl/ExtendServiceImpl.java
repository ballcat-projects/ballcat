package com.hccake.extend.mybatis.plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.extend.mybatis.plus.mapper.ExtendBaseMapper;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.Collection;

/**
 * 以前继承 com.baomidou.mybatisplus.extension.service.impl.ServiceImpl 的实现类，现在继承本类
 *
 * @author lingting 2020/7/21 10:00
 */
public class ExtendServiceImpl<M extends ExtendBaseMapper<T>, T> extends ServiceImpl<M, T> implements ExtendService<T> {

	@Override
	public int insertBatchSomeColumn(Collection<T> list) {
		return baseMapper.insertBatchSomeColumn(list);
	}

}
