package com.hccake.ballcat.system.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色绑定的用户
 *
 * @author ballcat code generator
 * @date 2019-09-12 20:39:31
 */
@Data
@ApiModel(value = "角色绑定的用户VO")
public class RoleBindUserVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户ID")
	private Integer userId;

	@ApiModelProperty(value = "登录账号")
	private String username;

	@ApiModelProperty(value = "昵称")
	private String nickname;

	@ApiModelProperty(value = "1:系统用户， 2：客户用户")
	private Integer type;

	@ApiModelProperty(value = "组织机构ID")
	private Integer organizationId;

	@ApiModelProperty(value = "组织机构名称")
	private String organizationName;

}
