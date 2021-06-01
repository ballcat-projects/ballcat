package com.hccake.ballcat.system.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 组织架构
 *
 * @author hccake 2020-09-23 20:39:40
 */
@Data
@TableName("sys_organization")
@ApiModel(value = "组织架构")
public class SysOrganization {

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
	 * 描述信息
	 */
	@ApiModelProperty(value = "描述信息")
	private String description;

	/**
	 * 排序字段，由小到大
	 */
	@ApiModelProperty(value = "排序字段，由小到大")
	private Integer sort;

	/**
	 * 创建者
	 */
	@ApiModelProperty(value = "创建者")
	private String createBy;

	/**
	 * 修改者
	 */
	@ApiModelProperty(value = "修改者")
	private String updateBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

}
