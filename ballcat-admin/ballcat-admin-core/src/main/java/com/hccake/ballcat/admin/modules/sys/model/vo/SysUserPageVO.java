package com.hccake.ballcat.admin.modules.sys.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "系统用户VO")
public class SysUserPageVO implements Serializable {

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

	@ApiModelProperty(value = "1:系统用户， 2：客户用户")
	private Integer type;

	/**
	 * 组织机构ID
	 */
	@ApiModelProperty(value = "组织机构ID")
	private Integer organizationId;

	/**
	 * 组织机构名称
	 */
	@ApiModelProperty(value = "组织机构名称")
	private String organizationName;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

}
