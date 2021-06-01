package com.hccake.ballcat.system.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lingting 2020-08-10 10:11
 */
@Getter
@Setter
@ApiModel("lov 模块 select 选项")
public class LovSelectOptions {

	/**
	 * key
	 */
	private Integer key;

	/**
	 * 值
	 */
	private String value;

	/**
	 * 标签
	 */
	private String label;

}
