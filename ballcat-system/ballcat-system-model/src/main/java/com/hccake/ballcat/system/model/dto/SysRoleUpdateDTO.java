package com.hccake.ballcat.system.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 角色修改DTO
 *
 * @author Hccake 2020-07-06
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(title = "角色修改DTO")
public class SysRoleUpdateDTO {

	private static final long serialVersionUID = 1L;

	@Schema(title = "角色编号")
	private Integer id;

	@NotBlank(message = "角色名称不能为空")
	@Schema(title = "角色名称")
	private String name;

	@Schema(title = "角色备注")
	private String remarks;

	@Schema(title = "数据权限")
	private Integer scopeType;

	@Schema(title = "数据范围资源，当数据范围类型为自定义时使用")
	private String scopeResources;

}
