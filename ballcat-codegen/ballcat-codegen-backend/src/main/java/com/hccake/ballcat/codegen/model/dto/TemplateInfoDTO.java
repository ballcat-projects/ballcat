package com.hccake.ballcat.codegen.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 模板信息
 *
 * @author hccake
 * @date 2020-06-19 18:09:08
 */
@Data
@ApiModel(value = "模板信息")
public class TemplateInfoDTO  {
	private static final long serialVersionUID = 1L;

	/**
	 * 模板名称
	 */
	@ApiModelProperty(value = "模板标题")
	private String title;
	/**
	 * 模板内容
	 */
	@ApiModelProperty(value = "模板内容")
	private String content;
	/**
	 * 模板引擎类型 1：velocity
	 */
	@ApiModelProperty(value = "模板引擎类型 1：velocity")
	private Integer engineType;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remarks;
}
