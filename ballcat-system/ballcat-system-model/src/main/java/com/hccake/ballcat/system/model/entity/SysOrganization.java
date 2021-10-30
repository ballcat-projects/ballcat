package com.hccake.ballcat.system.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hccake.ballcat.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织架构
 *
 * @author hccake 2020-09-23 20:39:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_organization")
@ApiModel(value = "组织架构")
public class SysOrganization extends LogicDeletedBaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "ID")
	private Integer id;

	/**
	 * 组织名称
	 */
	@ApiModelProperty(value = "组织名称")
	private String name;

	/**
	 * 父级ID
	 */
	@ApiModelProperty(value = "父级ID")
	private Integer parentId;

	/**
	 * 层级信息，从根节点到当前节点的最短路径，使用-分割节点ID
	 */
	@ApiModelProperty(value = "层级信息，从根节点到当前节点的最短路径，使用-分割节点ID")
	private String hierarchy;

	/**
	 * 当前节点深度
	 */
	@ApiModelProperty(value = "当前节点深度")
	private Integer depth;

	/**
	 * 排序字段，由小到大
	 */
	@ApiModelProperty(value = "排序字段，由小到大")
	private Integer sort;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remarks;

}
