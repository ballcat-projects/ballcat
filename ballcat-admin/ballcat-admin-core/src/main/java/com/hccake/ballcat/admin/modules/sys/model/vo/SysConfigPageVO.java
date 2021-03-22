package com.hccake.ballcat.admin.modules.sys.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统配置表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@Data
@ApiModel(value = "基础配置")
public class SysConfigPageVO {

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键ID")
	private Integer id;

	/**
	 * 配置名称
	 */
	@ApiModelProperty(value = "配置名称")
	private String name;

	/**
	 * 配置在缓存中的key名
	 */
	@ApiModelProperty(value = "配置在缓存中的key名")
	private String confKey;

	/**
	 * 配置值
	 */
	@ApiModelProperty(value = "配置值")
	private String confValue;

	/**
	 * 分类
	 */
	@ApiModelProperty(value = "分类")
	private String category;

	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String description;

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
