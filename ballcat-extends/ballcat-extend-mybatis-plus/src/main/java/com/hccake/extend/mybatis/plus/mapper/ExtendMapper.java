package com.hccake.extend.mybatis.plus.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.common.core.domain.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 所有的 Mapper接口 都需要继承当前接口 如果想自己定义其他的全局方法， 您的全局 BaseMapper 需要继承当前接口
 *
 * @author lingting 2020/5/27 11:39
 */
public interface ExtendMapper<T> extends BaseMapper<T> {

	/**
	 * 根据 PageParam 生成一个 IPage 实例
	 * @param pageParam 分页参数
	 * @param <V> 返回的 Record 对象
	 * @return IPage<V>
	 */
	default <V> IPage<V> prodPage(PageParam pageParam) {
		Page<V> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
		// TODO 等前端实现多列排序后，修改为支持多列排序
		String sortField = pageParam.getSortField();
		OrderItem orderItem = pageParam.isSortAsc() ? OrderItem.asc(sortField) : OrderItem.desc(sortField);
		page.addOrder(orderItem);
		return page;
	}

	/**
	 * 批量插入数据 实现类 {@link InsertBatchSomeColumn}
	 * @param list 数据列表
	 * @return int 改动行
	 * @author lingting 2020-08-26 22:11
	 */
	int insertBatchSomeColumn(@Param("list") Collection<T> list);

	/**
	 * 根据 entity 条件，查询全部记录（并翻页）
	 * @param page 分页查询条件（可以为 RowBounds.DEFAULT）
	 * @param queryWrapper 实体对象封装操作类（可以为 null）
	 * @return 分页参数
	 */
	<V> IPage<V> selectByPage(IPage<V> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

}