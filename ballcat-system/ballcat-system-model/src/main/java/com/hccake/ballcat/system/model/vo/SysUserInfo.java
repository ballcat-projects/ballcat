package com.hccake.ballcat.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统用户信息
 *
 * @author ballcat code generator
 * @date 2019-09-12 20:39:31
 */
@Data
@Schema(title = "系统用户信息")
public class SysUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@Schema(title = "用户ID")
	private Integer userId;

	/**
	 * 登录账号
	 */
	@Schema(title = "登录账号")
	private String username;

	/**
	 * 昵称
	 */
	@Schema(title = "昵称")
	private String nickname;

	/**
	 * 头像
	 */
	@Schema(title = "头像")
	private String avatar;

	/**
	 * 组织机构ID
	 */
	@Schema(title = "组织机构ID")
	private Integer organizationId;

	/**
	 * 用户类型
	 */
	@Schema(title = "用户类型：1-系统用户，2-客户用户")
	private Integer type;

}
