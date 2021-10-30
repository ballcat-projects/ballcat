package com.hccake.ballcat.i18n.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 国际化信息传输对象
 *
 * @author hccake 2021-08-06 10:48:25
 */
@Data
@ApiModel(value = "国际化信息传输对象")
public class I18nDataDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 语言标签
	 */
	@ApiModelProperty(value = "语言标签")
	private String languageTag;

	/**
	 * 唯一标识 = 业务:关键词
	 */
	@ApiModelProperty(value = "唯一标识 = 业务:关键词")
	private String code;

	/**
	 * 文本值，可以使用 { } 加角标，作为占位符
	 */
	@ApiModelProperty(value = "文本值，可以使用 { } 加角标，作为占位符")
	private String message;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remarks;

}