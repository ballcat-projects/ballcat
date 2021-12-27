package com.hccake.ballcat.tenant.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 租户数据源映射表分页视图对象
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@Data
@ApiModel(value = "租户数据源映射表分页视图对象")
public class SysTenantDatasourcePageVO {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Long id;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Long tenantId;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Long datasourceId;

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