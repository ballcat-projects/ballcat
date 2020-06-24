package com.hccake.ballcat.admin.modules.sys.model.vo;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 */
@Data
@ApiModel(value = "用户信息")
public class UserInfo implements Serializable {

	/**
	 * 用户基本信息
	 */
	@ApiModelProperty(value = "用户基本信息")
	private SysUser sysUser;

	/**
	 * 权限标识集合
	 */
	@ApiModelProperty(value = "权限标识集合")
	private List<String> permissions;

	/**
	 * 角色集合
	 */
	@ApiModelProperty(value = "角色标识集合")
	private List<String> roles;

	/**
	 * 角色ID集合
	 */
	@ApiModelProperty(value = "角色Id集合")
	private List<Integer> roleIds;

}
