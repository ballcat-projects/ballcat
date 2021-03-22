package com.hccake.ballcat.codegen.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 模板属性配置
 *
 * @author hccake
 * @date 2020-06-22 15:46:39
 */
@Data
@ApiModel(value = "模板属性配置")
public class TemplatePropertyPageVO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Integer id;

	/**
	 * 模板组ID
	 */
	@ApiModelProperty(value = "模板组ID")
	private Integer groupId;

	/**
	 * 标题
	 */
	@ApiModelProperty(value = "标题")
	private String title;

	/**
	 * 属性键
	 */
	@ApiModelProperty(value = "属性键")
	private String propKey;

	/**
	 * 默认值
	 */
	@ApiModelProperty(value = "默认值")
	private String defaultValue;

	/**
	 * 必填，1：是，0：否
	 */
	@ApiModelProperty(value = "必填，1：是，0：否")
	private Integer required;

	/**
	 * 备注信息
	 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;

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