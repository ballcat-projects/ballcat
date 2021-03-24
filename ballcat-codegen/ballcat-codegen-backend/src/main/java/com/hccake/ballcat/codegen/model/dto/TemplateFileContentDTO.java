package com.hccake.ballcat.codegen.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Hccake 2021/3/24
 * @version 1.0
 */
@Data
@ApiModel(value = "模板文件内容DTO")
public class TemplateFileContentDTO {

	@ApiModelProperty(value = "目录项ID")
	private Integer directoryEntryId;

	/**
	 * 模板内容
	 */
	@ApiModelProperty(value = "模板内容")
	private String content;

}
