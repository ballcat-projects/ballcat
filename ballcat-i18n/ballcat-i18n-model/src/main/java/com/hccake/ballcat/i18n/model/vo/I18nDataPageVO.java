package com.hccake.ballcat.i18n.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 国际化信息分页视图对象
 *
 * @author hccake 2021-08-04 11:31:49
 */
@Data
@ApiModel(value = "国际化信息分页视图对象")
public class I18nDataPageVO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Integer id;

	/**
	 * 业务
	 */
	@ApiModelProperty(value = "业务")
	private String business;

	/**
	 * 关键词
	 */
	@ApiModelProperty(value = "关键词")
	private String messageKey;

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
	 * 语言标识
	 */
	@ApiModelProperty(value = "语言标识")
	private String languageTag;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private LocalDateTime updateTime;

}