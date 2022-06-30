package com.hccake.ballcat.autoconfigure.web.pageable;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.hccake.ballcat.common.model.domain.PageableConstants.*;

/**
 * @author hccake
 */
@Data
@ConfigurationProperties("ballcat.pageable")
public class PageableProperties {

	/**
	 * 当前页的参数名
	 */
	private String pageParameterName = DEFAULT_PAGE_PARAMETER;

	/**
	 * 每页数据量的参数名
	 */
	private String sizeParameterName = DEFAULT_SIZE_PARAMETER;

	/**
	 * 排序规则的参数名
	 */
	private String sortParameterName = DEFAULT_SORT_PARAMETER;

	/**
	 * 分页查询的每页最大数据量
	 */
	private int maxPageSize = DEFAULT_MAX_PAGE_SIZE;

}
