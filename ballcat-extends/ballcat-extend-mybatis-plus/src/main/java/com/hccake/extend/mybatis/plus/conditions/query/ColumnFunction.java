package com.hccake.extend.mybatis.plus.conditions.query;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * 连表查询时，从其他表获取的查询条件
 *
 * @author hccake
 */
@FunctionalInterface
public interface ColumnFunction<T> extends SFunction<T, String> {

	/**
	 * 快捷的创建一个 ColumnFunction
	 * @param columnString 实际的 column
	 * @return ColumnFunction
	 */
	static <T> ColumnFunction<T> create(String columnString) {
		return o -> columnString;
	}

}
