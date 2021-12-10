package com.hccake.ballcat.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色绑定的用户
 *
 * @author ballcat code generator
 * @date 2019-09-12 20:39:31
 */
@Data
@Schema(title = "角色绑定的用户VO")
public class RoleBindUserVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(title = "用户ID")
	private Integer userId;

	@Schema(title = "登录账号")
	private String username;

	@Schema(title = "昵称")
	private String nickname;

	@Schema(title = "1:系统用户， 2：客户用户")
	private Integer type;

	@Schema(title = "组织机构ID")
	private Integer organizationId;

	@Schema(title = "组织机构名称")
	private String organizationName;

}
