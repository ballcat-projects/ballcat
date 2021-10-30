package com.hccake.ballcat.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hccake.ballcat.common.model.entity.LogicDeletedBaseEntity;
import com.hccake.extend.mybatis.plus.alias.TableAlias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "系统用户表")
public class SysUser extends LogicDeletedBaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableId
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
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	private String password;

	/**
	 * md5密码盐
	 */
	@ApiModelProperty(value = "md5密码盐")
	private String salt;

	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像")
	private String avatar;

	/**
	 * 性别(0-默认未知,1-男,2-女)
	 */
	@ApiModelProperty(value = "性别(0-默认未知,1-男,2-女)")
	private Integer sex;

	/**
	 * 电子邮件
	 */
	@ApiModelProperty(value = "电子邮件")
	private String email;

	/**
	 * 电话
	 */
	@ApiModelProperty(value = "电话")
	private String phone;

	/**
	 * 状态(1-正常,0-冻结)
	 */
	@ApiModelProperty(value = "状态(1-正常, 0-冻结)")
	private Integer status;

	/**
	 * 组织机构ID
	 */
	@ApiModelProperty(value = "组织机构ID")
	private Integer organizationId;

	/**
	 * 用户类型
	 */
	@ApiModelProperty(value = "1:系统用户， 2：客户用户")
	private Integer type;

}
