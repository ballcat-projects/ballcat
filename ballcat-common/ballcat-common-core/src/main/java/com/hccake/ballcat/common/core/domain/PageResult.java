package com.hccake.ballcat.common.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 分页返回结果
 *
 * @author Hccake 2021/1/18
 * @version 1.0
 */
@Data
@ApiModel("分页返回结果")
public class PageResult<T> {

	/**
	 * 查询数据列表
	 */
	@ApiModelProperty(value = "分页数据", required = true)
	protected List<T> records = Collections.emptyList();

	/**
	 * 总数
	 */
	@ApiModelProperty(value = "数据总量", required = true)
	protected Long total = 0L;

	public PageResult() {
	}

	public PageResult(long total) {
		this.total = total;
	}

	public PageResult(List<T> records, long total) {
		this.records = records;
		this.total = total;
	}

}
