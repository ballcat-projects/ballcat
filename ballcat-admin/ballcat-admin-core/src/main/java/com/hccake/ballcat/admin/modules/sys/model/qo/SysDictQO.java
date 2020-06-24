package com.hccake.ballcat.admin.modules.sys.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

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
	 * 编号
	 */
	@ApiModelProperty(value = "编号")
	private Integer id;

}
