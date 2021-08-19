package com.hccake.ballcat.common.i18n;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 对标于 message bundle 的文件消息的抽象
 *
 * @author hccake
 */
@Data
@ApiModel("国际化信息")
public class I18nMessage {

	/**
	 * 国际化标识
	 */
	@NotEmpty(message = "{i18nMessage.code}：{}")
	@ApiModelProperty(value = "国际化标识")
	private String code;

	/**
	 * 消息
	 */
	@NotEmpty(message = "{i18nMessage.message}：{}")
	@ApiModelProperty(value = "文本值，可以使用 { } 加角标，作为占位符")
	private String message;

	/**
	 * 地区语言标签
	 */
	@NotEmpty(message = "{i18nMessage.languageTag}：{}")
	@ApiModelProperty(value = "语言标签")
	private String languageTag;

}
