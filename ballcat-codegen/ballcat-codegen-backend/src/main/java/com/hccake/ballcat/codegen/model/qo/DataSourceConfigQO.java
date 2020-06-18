package com.hccake.ballcat.codegen.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据源 查询对象
 *
 * @author hccake
 * @date 2020-06-17 10:24:47
 */
@Data
@ApiModel(value = "数据源查询对象")
public class DataSourceConfigQO {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Integer id;
}
