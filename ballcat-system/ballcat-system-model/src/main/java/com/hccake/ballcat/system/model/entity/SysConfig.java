package com.hccake.ballcat.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hccake.ballcat.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统配置表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_config")
@ApiModel(value = "基础配置")
public class SysConfig extends LogicDeletedBaseEntity {

	/**
	 * 主键
	 */
	@TableId
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
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remarks;

}
