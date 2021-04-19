package com.hccake.ballcat.admin.modules.system.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统用户信息
 *
 * @author ballcat code generator
 * @date 2019-09-12 20:39:31
 */
@Data
@ApiModel(value = "系统用户信息")
public class SysUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
	private Integer userId;

	/**
	 * 登录账号
	 */
	@ApiModelProperty(value = "登录账号")
	private String username;

	/**
	 * 昵称
	 */
	@ApiModelProperty(value = "昵称")
	private String nickname;

	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像")
	private String avatar;

	/**
	 * 用户类型
	 */
	@ApiModelProperty(value = "用户类型：1-系统用户，2-客户用户")
	private Integer type;

}
