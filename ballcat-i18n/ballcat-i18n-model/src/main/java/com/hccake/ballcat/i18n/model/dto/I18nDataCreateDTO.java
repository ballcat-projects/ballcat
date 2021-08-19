package com.hccake.ballcat.i18n.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 国际化信息传输对象
 *
 * @author hccake 2021-08-06 10:48:25
 */
@Data
@ApiModel(value = "国际化信息传输对象")
public class I18nDataCreateDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 唯一标识 = 业务:关键词
	 */
	@NotEmpty(message = "{i18nMessage.code}：{}")
	@ApiModelProperty(value = "唯一标识 = 业务:关键词")
	private String code;

	/**
	 * 语言文本列表
	 */
	@Valid
	@NotNull(message = "{i18nData.languageTexts}: {}")
	@Size(min = 1, message = "{i18nData.languageTexts}: {}")
	@ApiModelProperty(value = "语言文本列表")
	private List<LanguageText> languageTexts;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

	/**
	 * 语言文本
	 */
	@Data
	@ApiModel(value = "语言文本信息")
	public static class LanguageText {

		/**
		 * 语言标签
		 */
		@NotEmpty(message = "{i18nMessage.languageTag}：{}")
		@ApiModelProperty(value = "语言标签")
		private String languageTag;

		/**
		 * 文本值，可以使用 { } 加角标，作为占位符
		 */
		@NotEmpty(message = "{i18nMessage.message}：{}")
		@ApiModelProperty(value = "文本值，可以使用 { } 加角标，作为占位符")
		private String message;

	}

}