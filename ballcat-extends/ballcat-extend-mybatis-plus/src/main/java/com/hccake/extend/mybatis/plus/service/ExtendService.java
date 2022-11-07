package com.hccake.extend.mybatis.plus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * 以前继承 com.baomidou.mybatisplus.extension.service.IService 的实现类，现在继承当前类
 *
 * @author lingting 2020/7/21 9:58
 * @update author.zero 2022/11/07 11:46 修改使用继承IService
 */
public interface ExtendService<T> extends IService<T> {

	/**
	 * 批量插入数据
	 * @param list 数据列表
	 * @return int 改动行
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
	 */
	boolean saveBatchSomeColumn(Collection<T> list, int batchSize);

}
