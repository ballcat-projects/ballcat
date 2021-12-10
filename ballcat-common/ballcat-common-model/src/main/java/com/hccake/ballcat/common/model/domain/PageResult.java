package com.hccake.ballcat.common.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(title = "分页返回结果")
public class PageResult<T> {

	/**
	 * 查询数据列表
	 */
	@Schema(title = "分页数据")
	protected List<T> records = Collections.emptyList();

	/**
	 * 总数
	 */
	@Schema(title = "数据总量")
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
