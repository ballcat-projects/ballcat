package com.hccake.ballcat.tenant.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 租户表 查询对象
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@Data
@ApiModel(value = "租户表查询对象")
public class SysTenantQO {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long tenantId;

}