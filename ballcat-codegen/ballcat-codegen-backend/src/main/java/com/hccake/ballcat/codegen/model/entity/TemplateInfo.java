package com.hccake.ballcat.codegen.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 模板信息
 *
 * @author hccake
 * @date 2020-06-19 18:09:08
 */
@Data
@TableName("gen_template_info")
@ApiModel(value = "模板信息")
public class TemplateInfo {

	private static final long serialVersionUID = 1L;

	/**
	 * 目录项ID
	 */
	@TableId(type = IdType.INPUT)
	@ApiModelProperty(value = "目录项ID")
	private Integer directoryEntryId;

	/**
	 * 模板组Id
	 */
	@ApiModelProperty(value = "模板组Id")
	private Integer groupId;

	/**
	 * 模板名称
	 */
	@ApiModelProperty(value = "模板标题")
	private String title;

	/**
	 * 模板内容
	 */
	@ApiModelProperty(value = "模板内容")
	private String content;

	/**
	 * 模板引擎类型 1：velocity
	 */
	@ApiModelProperty(value = "模板引擎类型 1：velocity")
	private Integer engineType;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remarks;

	/**
	 * 逻辑删除
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "逻辑删除")
	private Long deleted;

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
