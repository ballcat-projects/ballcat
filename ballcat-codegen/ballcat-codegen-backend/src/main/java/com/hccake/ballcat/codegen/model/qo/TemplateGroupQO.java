package com.hccake.ballcat.codegen.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 模板组 查询对象
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
@Data
@ApiModel(value = "模板组查询对象")
public class TemplateGroupQO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Integer id;

}