package com.hccake.ballcat.admin.modules.system.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 角色修改DTO
 *
 * @author Hccake 2020-07-06
 */
@Data
@ApiModel(value = "角色修改DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysRoleUpdateDTO {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "角色编号")
	private Integer id;

	@NotBlank(message = "角色名称不能为空")
	@ApiModelProperty(value = "角色名称")
	private String name;

	@ApiModelProperty(value = "角色备注")
	private String note;

	@ApiModelProperty(value = "数据权限")
	private Integer scopeType;

}
