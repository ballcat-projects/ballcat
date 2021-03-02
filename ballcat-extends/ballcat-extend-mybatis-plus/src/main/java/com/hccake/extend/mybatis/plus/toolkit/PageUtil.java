package com.hccake.extend.mybatis.plus.toolkit;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.common.model.domain.PageParam;

import java.util.List;

/**
 * @author Hccake 2021/1/19
 * @version 1.0
 */
public final class PageUtil {

	/**
	 * 根据 PageParam 生成一个 IPage 实例
	 * @param pageParam 分页参数
	 * @param <V> 返回的 Record 对象
	 * @return IPage<V>
	 */
	public static <V> IPage<V> prodPage(PageParam pageParam) {
		Page<V> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
		List<PageParam.Sort> sorts = pageParam.getSorts();
		for (PageParam.Sort sort : sorts) {
			OrderItem orderItem = sort.isAsc() ? OrderItem.asc(sort.getField()) : OrderItem.desc(sort.getField());
			page.addOrder(orderItem);
		}
		return page;
	}

}
