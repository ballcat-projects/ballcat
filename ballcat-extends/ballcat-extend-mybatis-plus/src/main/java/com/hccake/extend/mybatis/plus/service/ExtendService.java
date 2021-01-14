package com.hccake.extend.mybatis.plus.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

/**
 * 以前继承 com.baomidou.mybatisplus.extension.service.IService 的实现类，现在继承当前类
 *
 * @author lingting 2020/7/21 9:58
 */
public interface ExtendService<T> extends IService<T> {

	/**
	 * 批量插入数据
	 * @param list 数据列表
	 * @return int 改动行
	 * @author lingting 2020-08-26 22:11
	 */
	int insertBatchSomeColumn(Collection<T> list);

}
