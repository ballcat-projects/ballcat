package com.hccake.ballcat.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hccake.ballcat.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(title = "基础配置")
public class SysConfig extends LogicDeletedBaseEntity {

	/**
	 * 主键
	 */
	@TableId
	@Schema(title = "主键ID")
	private Integer id;

	/**
	 * 配置名称
	 */
	@Schema(title = "配置名称")
	private String name;

	/**
	 * 配置在缓存中的key名
	 */
	@Schema(title = "配置在缓存中的key名")
	private String confKey;

	/**
	 * 配置值
	 */
	@Schema(title = "配置值")
	private String confValue;

	/**
	 * 分类
	 */
	@Schema(title = "分类")
	private String category;

	/**
	 * 备注
	 */
	@Schema(title = "备注")
	private String remarks;

}
