package com.hccake.ballcat.admin.modules.system.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "角色")
public class SysRolePageVO {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "角色编号")
	private Integer id;

	@ApiModelProperty(value = "角色名称")
	private String name;

	@ApiModelProperty(value = "角色标识")
	private String code;

	@ApiModelProperty(value = "角色备注")
	private String note;

	@ApiModelProperty("角色类型，1：系统角色 2：业务角色")
	private Integer type;

	@ApiModelProperty("数据权限：1全部，2本人，3本人及子部门，4本部门")
	private Integer scopeType;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

}
