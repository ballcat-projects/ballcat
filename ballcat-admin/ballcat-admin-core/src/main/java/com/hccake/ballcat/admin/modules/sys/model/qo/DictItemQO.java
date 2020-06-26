package com.hccake.ballcat.admin.modules.sys.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 字典项 查询对象
 *
 * @author hccake
 * @date 2020-03-26 18:32:06
 */
@Data
@ApiModel(value = "字典项查询对象")
public class DictItemQO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Integer id;

}
