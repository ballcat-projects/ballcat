package com.hccake.ballcat.admin.modules.system.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典表
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
@Data
@ApiModel(value = "字典表")
public class SysDictPageVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@ApiModelProperty(value = "编号")
	private Integer id;

	/**
	 * 标识
	 */
	@ApiModelProperty(value = "标识")
	private String code;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String title;

	/**
	 * Hash值
	 */
	@ApiModelProperty(value = "Hash值")
	private String hashCode;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remarks;

	/**
	 * 可编辑的
	 */
	@ApiModelProperty(value = "1：是 0：否")
	private Integer editable;

	/**
	 * 数据类型
	 */
	@ApiModelProperty("数据类型,1:Number 2:String 3:Boolean")
	private Integer valueType;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

}
