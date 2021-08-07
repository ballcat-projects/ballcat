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

	@ApiModelProperty(value = "国际化标识")
	private String code;

	@ApiModelProperty(value = "国际化标识")
	private String languageTag;

}