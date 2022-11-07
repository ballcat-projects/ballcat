package com.hccake.extend.mybatis.plus.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.service.ExtendService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 以前继承 com.baomidou.mybatisplus.extension.service.impl.ServiceImpl 的实现类，现在继承本类
 *
 * @author lingting 2020/7/21 10:00
 * @update author.zero 2022/11/07 11:46 修改使用继承ServiceImpl
 */
@SuppressWarnings("unchecked")
public class ExtendServiceImpl<M extends ExtendMapper<T>, T> extends ServiceImpl<M, T> implements ExtendService<T> {

	/**
	 * 批量插入数据
	 * @param list 数据列表
	 * @param batchSize 批次插入数据量
	 * @return int 改动行
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveBatchSomeColumn(Collection<T> list, int batchSize) {
		if (CollUtil.isEmpty(list)) {
			return false;
		}
		List<List<T>> segmentDataList = CollectionUtil.split(list, batchSize);
		for (List<T> data : segmentDataList) {
			baseMapper.insertBatchSomeColumn(data);
		}
		return true;
	}

}
