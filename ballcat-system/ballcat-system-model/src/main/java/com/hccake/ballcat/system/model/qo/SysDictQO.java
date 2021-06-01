package com.hccake.ballcat.system.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 字典表 查询对象
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
@Data
@ApiModel(value = "字典表查询对象")
public class SysDictQO {

	private static final long serialVersionUID = 1L;

	/**
	 * 字典标识
	 */
	@ApiModelProperty(value = "字典标识")
	private String code;

	/**
	 * 字典名称
	 */
	@ApiModelProperty(value = "字典名称")
	private String title;

}
