package com.hccake.ballcat.admin.modules.sys.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 字典项
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
@Data
@ApiModel(value = "字典项VO")
public class DictItemVO {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("id")
	private Integer id;

	/**
	 * 数据值
	 */
	@ApiModelProperty(value = "数据值")
	private String value;

	/**
	 * 标签
	 */
	@ApiModelProperty(value = "文本值")
	private String name;

}
