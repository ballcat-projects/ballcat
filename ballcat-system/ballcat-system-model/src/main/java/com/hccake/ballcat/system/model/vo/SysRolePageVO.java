package com.hccake.ballcat.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author ballcat
 * @since 2017-10-29
 */
@Data
@Schema(title = "角色")
public class SysRolePageVO {

	private static final long serialVersionUID = 1L;

	@Schema(title = "角色编号")
	private Integer id;

	@Schema(title = "角色名称")
	private String name;

	@Schema(title = "角色标识")
	private String code;

	@Schema(title = "角色类型，1：系统角色 2：业务角色")
	private Integer type;

	@Schema(title = "数据权限类型")
	private Integer scopeType;

	@Schema(title = "数据范围资源，当数据范围类型为自定义时使用")
	private String scopeResources;

	@Schema(title = "角色备注")
	private String remarks;

	@Schema(title = "创建时间")
	private LocalDateTime createTime;

	@Schema(title = "更新时间")
	private LocalDateTime updateTime;

}
