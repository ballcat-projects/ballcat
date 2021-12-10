package com.hccake.ballcat.system.model.qo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/22 17:22
 */
@Data
@Schema(title = "系统用户查询对象")
@ParameterObject
public class SysUserQO {

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
	 * 状态(1-正常,2-冻结)
	 */
	@Schema(title = "状态(1-正常,2-冻结)")
	private Integer status;

	/**
	 * 组织机构ID
	 */
	@Schema(title = "organizationId")
	private List<Integer> organizationId;

	@Schema(title = "用户类型:1:系统用户， 2：客户用户")
	private Integer type;

	@Schema(title = "开始时间")
	private String startTime;

	@Schema(title = "结束时间")
	private String endTime;

}
