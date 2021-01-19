package com.hccake.ballcat.common.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 下拉框所对应的视图类
 *
 * @author Hccake
 */
@Data
@ApiModel("下拉框数据")
public class SelectData<T> {

	/**
	 * 显示的数据
	 */
	@ApiModelProperty(value = "显示的数据", required = true)
	private String name;

	/**
	 * 选中获取的属性
	 */
	@ApiModelProperty(value = "选中获取的属性", required = true)
	private String value;

	/**
	 * 是否被选中
	 */
	@ApiModelProperty(value = "是否被选中")
	private String selected;

	/**
	 * 是否禁用
	 */
	@ApiModelProperty(value = "是否禁用")
	private String disabled;

	/**
	 * 分组标识
	 */
	@ApiModelProperty(value = "分组标识")
	private String type;

	/**
	 * 扩展对象
	 */
	@ApiModelProperty(value = "扩展对象")
	private T extendObj;

}
