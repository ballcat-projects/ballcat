package com.hccake.ballcat.common.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 下拉框所对应的视图类
 *
 * @author Hccake
 */
@Data
@Schema(title = "下拉框数据")
public class SelectData<T> {

	/**
	 * 显示的数据
	 */
	@Schema(title = "显示的数据", required = true)
	private String name;

	/**
	 * 选中获取的属性
	 */
	@Schema(title = "选中获取的属性", required = true)
	private Object value;

	/**
	 * 是否被选中
	 */
	@Schema(title = "是否被选中")
	private Boolean selected;

	/**
	 * 是否禁用
	 */
	@Schema(title = "是否禁用")
	private Boolean disabled;

	/**
	 * 分组标识
	 */
	@Schema(title = "分组标识")
	private String type;

	/**
	 * 扩展对象
	 */
	@Schema(title = "扩展对象")
	private T extendObj;

}
