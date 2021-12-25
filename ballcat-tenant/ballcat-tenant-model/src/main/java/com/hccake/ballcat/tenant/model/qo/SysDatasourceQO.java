package com.hccake.ballcat.tenant.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据源表 查询对象
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@Data
@ApiModel(value = "数据源表查询对象")
public class SysDatasourceQO {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Long id;

}