package com.hccake.extend.mybatis.plus.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.Collection;

/**
 * 以前继承 com.baomidou.mybatisplus.extension.service.impl.ServiceImpl 的实现类，现在继承本类
 *
 * @author lingting 2020/7/21 10:00
 */
public class ExtendServiceImpl<M extends ExtendMapper<T>, T> extends ServiceImpl<M, T> implements ExtendService<T> {

	@Override
	public boolean saveBatchSomeColumn(Collection<T> list) {
		if (CollectionUtil.isEmpty(list)) {
			return false;
		}
		int i = baseMapper.insertBatchSomeColumn(list);
		return SqlHelper.retBool(i);
	}

}
