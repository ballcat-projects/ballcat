package com.hccake.ballcat.system.model.qo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

/**
 * 角色查询对象
 *
 * @author Hccake
 */
@Data
@Schema(title = "角色查询对象")
@ParameterObject
public class SysRoleQO {

	private static final long serialVersionUID = 1L;

	@Schema(title = "角色名称")
	private String name;

	@Schema(title = "角色标识")
	private String code;

	@Schema(title = "开始时间")
	private String startTime;

	@Schema(title = "结束时间")
	private String endTime;

}
