package com.hccake.ballcat.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hccake.ballcat.common.model.entity.LogicDeletedBaseEntity;
import com.hccake.extend.mybatis.plus.alias.TableAlias;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统用户表
 *
 * @author ballcat code generator
 * @date 2019-09-12 20:39:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableAlias("su")
@TableName("sys_user")
@Schema(title = "系统用户表")
public class SysUser extends LogicDeletedBaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableId
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
	 * 密码
	 */
	@Schema(title = "密码")
	private String password;

	/**
	 * md5密码盐
	 */
	@Schema(title = "md5密码盐")
	private String salt;

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

	/**
	 * 组织机构ID
	 */
	@Schema(title = "组织机构ID")
	private Integer organizationId;

	/**
	 * 用户类型
	 */
	@Schema(title = "1:系统用户， 2：客户用户")
	private Integer type;

}
