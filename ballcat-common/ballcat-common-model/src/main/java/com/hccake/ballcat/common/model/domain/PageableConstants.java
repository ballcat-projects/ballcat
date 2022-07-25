package com.hccake.ballcat.common.model.domain;

import cn.hutool.core.collection.CollUtil;

import java.util.Set;

/**
 * @author hccake
 */
public final class PageableConstants {

	private PageableConstants() {
	}

	/**
	 * 排序的 Field 部分的正则
	 */
	public static final String SORT_FILED_REGEX = "(([A-Za-z0-9_]{1,10}\\.)?[A-Za-z0-9_]{1,64})";

	/**
	 * 排序的 order 部分的正则
	 */
	public static final String SORT_FILED_ORDER = "(desc|asc)";

	/**
	 * 完整的排序规则正则
	 */
	public static final String SORT_REGEX = "^" + PageableConstants.SORT_FILED_REGEX + "(,"
			+ PageableConstants.SORT_FILED_ORDER + ")*$";

	/**
	 * 默认的当前页数的参数名
	 */
	public static final String DEFAULT_PAGE_PARAMETER = "page";

	/**
	 * 默认的单页条数的参数名
	 */
	public static final String DEFAULT_SIZE_PARAMETER = "size";

	/**
	 * 默认的排序参数的参数名
	 */
	public static final String DEFAULT_SORT_PARAMETER = "sort";

	/**
	 * 默认的最大单页条数
	 */
	public static final int DEFAULT_MAX_PAGE_SIZE = 100;

	/**
	 * 升序关键字
	 */
	public static final String ASC = "asc";

	/**
	 * SQL 关键字
	 */
	public static final Set<String> SQL_KEYWORDS = CollUtil.newHashSet("master", "truncate", "insert", "select",
			"delete", "update", "declare", "alter", "drop", "sleep");

	/**
	 * 升序关键字
	 * @deprecated 已过期，使用 sort 进行传参
	 */
	@Deprecated
	public static final String SORT_ORDERS = "sortOrders";

	/**
	 * 升序关键字
	 * @deprecated 已过期，使用 sort 进行传参
	 */
	@Deprecated
	public static final String SORT_FIELDS = "sortFields";

}
