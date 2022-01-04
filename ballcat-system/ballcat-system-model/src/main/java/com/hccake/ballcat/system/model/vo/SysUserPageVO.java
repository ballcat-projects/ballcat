package com.hccake.ballcat.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统用户表
 *
 * @author ballcat code generator
 * @date 2019-09-12 20:39:31
 */
@Data
@Schema(title = "系统用户VO")
public class SysUserPageVO implements Serializable {

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
	 * 性别(0-默认未知,1-男,2-女)
	 */
	@Schema(title = "性别(0-默认未知,1-男,2-女)")
	private Integer sex;

	/**
	 * 电子邮件
	 */
	@Schema(title = "电子邮件")
	private String email;

	/**
	 * 电话
	 */
	@Schema(title = "电话")
	private String phone;

	/**
	 * 状态(1-正常,0-冻结)
	 */
	@Schema(title = "状态(1-正常, 0-冻结)")
	private Integer status;

	@Schema(title = "用户类型：1-系统用户，2-客户用户")
	private Integer type;

	/**
	 * 组织机构ID
	 */
	@Schema(title = "组织机构ID")
	private Integer organizationId;

	/**
	 * 组织机构名称
	 */
	@Schema(title = "组织机构名称")
	private String organizationName;

	/**
	 * 创建时间
	 */
	@Schema(title = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Schema(title = "更新时间")
	private LocalDateTime updateTime;

}
