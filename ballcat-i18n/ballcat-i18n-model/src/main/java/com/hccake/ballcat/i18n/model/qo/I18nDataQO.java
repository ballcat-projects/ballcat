package com.hccake.ballcat.i18n.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 国际化信息 查询对象
 *
 * @author hccake 2021-08-06 10:48:25
 */
@Data
@ApiModel(value = "国际化信息查询对象")
public class I18nDataQO {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "国际化标识")
	private String code;

	@ApiModelProperty(value = "文本信息")
	private String message;

	@ApiModelProperty(value = "语言标签")
	private String languageTag;

}