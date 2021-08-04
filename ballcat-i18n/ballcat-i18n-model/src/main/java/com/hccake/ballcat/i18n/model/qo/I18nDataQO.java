package com.hccake.ballcat.i18n.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 国际化信息 查询对象
 *
 * @author hccake 2021-08-04 11:31:49
 */
@Data
@ApiModel(value = "国际化信息查询对象")
public class I18nDataQO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Integer id;

}