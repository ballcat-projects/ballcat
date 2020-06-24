package com.hccake.ballcat.admin.modules.sys.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/22 17:22
 */
@Data
@ApiModel("系统用户查询对象")
public class SysUserQO {

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

	@ApiModelProperty(value = "开始时间")
	private String startTime;

	@ApiModelProperty(value = "结束时间")
	private String endTime;

	@ApiModelProperty(value = "用户类型:1:系统用户， 2：客户用户")
	private Integer type;

}
