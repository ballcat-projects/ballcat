package com.hccake.ballcat.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hccake.ballcat.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典表
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict")
@Schema(title = "字典表")
public class SysDict extends LogicDeletedBaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId
	@Schema(title = "编号")
	private Integer id;

	/**
	 * 标识
	 */
	@Schema(title = "标识")
	private String code;

	/**
	 * 名称
	 */
	@Schema(title = "名称")
	private String title;

	/**
	 * Hash值
	 */
	@Schema(title = "Hash值")
	private String hashCode;

	/**
	 * 数据类型
	 */
	@Schema(title = "数据类型", description = "1:Number 2:String 3:Boolean")
	private Integer valueType;

	/**
	 * 备注
	 */
	@Schema(title = "备注")
	private String remarks;

}
