package com.hccake.ballcat.admin.modules.sys.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonRegexDesensitize;
import com.hccake.ballcat.common.desensitize.enums.RegexDesensitizationTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统用户表
 *
 * @author ballcat code generator
 * @date 2019-09-12 20:39:31
 */
@Data
public class SysUserDTO {

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Integer userId;

	/**
	 * 前端传入密码
	 */
	@JsonRegexDesensitize(type = RegexDesensitizationTypeEnum.ENCRYPTED_PASSWORD)
	@ApiModelProperty(value = "前端传入密码")
	private String pass;

	/**
	 * 用户明文密码, 不参与前后端交互
	 */
	@JsonIgnore
	private String password;

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
	 * 状态(1-正常,2-冻结)
	 */
	@ApiModelProperty(value = "状态(1-正常,2-冻结)")
	private Integer status;

	/**
	 * 组织机构ID
	 */
	@ApiModelProperty(value = "organizationId")
	private Integer organizationId;

}
