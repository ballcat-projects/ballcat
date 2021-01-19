package com.hccake.ballcat.common.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 分页查询参数
 *
 * @author Hccake 2021/1/18
 * @version 1.0
 */
@Data
@ApiModel("分页查询参数")
public class PageParam {

	/**
	 * 当前页
	 */
	@ApiModelProperty(value = "当前页码，从 1 开始", required = true, example = "1")
	@NotNull(message = "当前页码不能为空")
	@Min(value = 1, message = "当前页不能小于 1")
	private long current = 1;

	/**
	 * 每页显示条数，默认 10
	 */
	@ApiModelProperty(value = "每页条数，最大值为 100", required = true, example = "10")
	@NotNull(message = "每页条数不能为空")
	@Range(min = 1, max = 100, message = "条数范围为 [1, 100]")
	private long size = 10;

	/**
	 * 排序字段
	 */
	@ApiModelProperty(value = "排序字段")
	private String sortField;

	/**
	 * 是否正序排序
	 */
	@ApiModelProperty(value = "是否正序排序")
	private boolean sortAsc;

}
