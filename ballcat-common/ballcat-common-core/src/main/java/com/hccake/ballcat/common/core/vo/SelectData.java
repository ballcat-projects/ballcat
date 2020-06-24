package com.hccake.ballcat.common.core.vo;

import lombok.Data;

import java.util.Map;

/**
 * 下拉框所对应的视图类
 *
 * @author Hccake
 */
@Data
public class SelectData<T> {

	/**
	 * 显示的数据
	 */
	private String name;

	/**
	 * 选中获取的属性
	 */
	private String value;

	/**
	 * 是否被选中
	 */
	private String selected;

	/**
	 * 是否禁用
	 */
	private String disabled;

	/**
	 * 分组标识
	 */
	private String type;

	/**
	 * 扩展对象
	 */
	private T extendObj;

}
