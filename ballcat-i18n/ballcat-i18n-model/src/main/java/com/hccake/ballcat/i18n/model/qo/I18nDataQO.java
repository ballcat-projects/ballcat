package com.hccake.ballcat.i18n.model.qo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

/**
 * 国际化信息 查询对象
 *
 * @author hccake 2021-08-06 10:48:25
 */
@Data
@Schema(title = "国际化信息查询对象")
@ParameterObject
public class I18nDataQO {

	private static final long serialVersionUID = 1L;

	@Schema(title = "国际化标识")
	private String code;

	@Schema(title = "文本信息")
	private String message;

	@Schema(title = "语言标签")
	private String languageTag;

}