package com.hccake.ballcat.system.model.vo;

import com.hccake.ballcat.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织架构
 *
 * @author hccake 2020-09-23 20:39:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(title = "组织架构")
public class SysOrganizationVO extends BaseEntity {

	/**
	 * ID
	 */
	@Schema(title = "ID")
	private Integer id;

	/**
	 * 组织名称
	 */
	@Schema(title = "组织名称")
	private String name;

	/**
	 * 父级ID
	 */
	@Schema(title = "父级ID")
	private Integer parentId;

	/**
	 * 层级信息，从根节点到当前节点的最短路径，使用-分割节点ID
	 */
	@Schema(title = "层级信息，从根节点到当前节点的最短路径，使用-分割节点ID")
	private String hierarchy;

	/**
	 * 当前节点深度
	 */
	@Schema(title = "当前节点深度")
	private Integer depth;

	/**
	 * 排序字段，由小到大
	 */
	@Schema(title = "排序字段，由小到大")
	private Integer sort;

	/**
	 * 备注
	 */
	@Schema(title = "备注")
	private String remarks;

}
