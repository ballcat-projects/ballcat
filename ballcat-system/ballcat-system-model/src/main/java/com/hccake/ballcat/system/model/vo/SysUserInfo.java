package com.hccake.ballcat.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统用户信息
 *
 * @author ballcat code generator 2019-09-12 20:39:31
 */
@Data
@Schema(title = "系统用户信息")
public class SysUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@Schema(title = "用户ID")
	private Long userId;

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
	private Long organizationId;

	/**
	 * 性别(0-默认未知,1-男,2-女)
	 */
	@Schema(title = "性别(0-默认未知,1-男,2-女)")
	private Integer gender;

	/**
	 * 电子邮件
	 */
	@Schema(title = "电子邮件")
	private String email;

	/**
	 * 手机号
	 */
	@Schema(title = "手机号")
	private String phoneNumber;

	/**
	 * 用户类型
	 */
	@Schema(title = "用户类型：1-系统用户，2-客户用户")
	private Integer type;

}
