package com.hccake.ballcat.i18n.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 国际化信息
 *
 * @author hccake 2021-08-04 11:31:49
 */
@Data
@TableName("i18n_data")
@ApiModel(value = "国际化信息")
public class I18nData {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
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
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(value = "修改时间")
	private LocalDateTime updateTime;

}
