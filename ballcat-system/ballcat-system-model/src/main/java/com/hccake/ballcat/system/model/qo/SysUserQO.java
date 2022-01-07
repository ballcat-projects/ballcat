package com.hccake.ballcat.system.model.qo;

import io.swagger.v3.oas.annotations.Parameter;
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
	@Parameter(description = "登录账号")
	private String username;

	/**
	 * 昵称
	 */
	@Parameter(description = "昵称")
	private String nickname;

	/**
	 * 性别(0-默认未知,1-男,2-女)
	 */
	@Parameter(description = "性别(0-默认未知,1-男,2-女)")
	private Integer sex;

	/**
	 * 电子邮件
	 */
	@Parameter(description = "电子邮件")
	private String email;

	/**
	 * 电话
	 */
	@Parameter(description = "电话")
	private String phone;

	/**
	 * 状态(1-正常,2-冻结)
	 */
	@Parameter(description = "状态(1-正常,2-冻结)")
	private Integer status;

	/**
	 * 组织机构ID
	 */
	@Parameter(description = "organizationId")
	private List<Integer> organizationId;

	@Parameter(description = "用户类型:1:系统用户， 2：客户用户")
	private Integer type;

	@Parameter(description = "开始时间")
	private String startTime;

	@Parameter(description = "结束时间")
	private String endTime;

}
