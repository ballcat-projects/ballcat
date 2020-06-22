package com.hccake.ballcat.codegen.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 模板信息
 *
 * @author hccake
 * @date 2020-06-19 18:09:08
 */
@Accessors(chain = true)
@Data
@ApiModel(value = "模板文件")
public class TemplateFile {
	private static final long serialVersionUID = 1L;

	/**
	 * 文件名称
	 */
	@ApiModelProperty(value = "文件名称")
	private String fileName;

	/**
	 * 文件路径
	 */
	@ApiModelProperty(value = "文件路径")
	private String filePath;
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
}
