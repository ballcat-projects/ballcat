package com.hccake.extend.mybatis.plus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 所有的 Mapper接口 都需要继承当前接口 如果想自己定义其他的全局方法， 您的全局 BaseMapper 需要继承当前接口
 *
 * @author lingting 2020/5/27 11:39
 */
public interface ExtendBaseMapper<T> extends BaseMapper<T> {

	/**
	 * 批量插入数据 实现类 {@link InsertBatchSomeColumn}
	 * @param list 数据列表
	 * @return int 改动行
	 * @author lingting 2020-08-26 22:11
	 */
	int insertBatchSomeColumn(@Param("list") Collection<T> list);

}