package com.hccake.ballcat.admin.modules.sys.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统配置表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@Data
@ApiModel(value = "基础配置")
public class SysConfigQO {

	/**
	 * 配置名称
	 */
	@ApiModelProperty(value = "配置名称")
	private String name;

	/**
	 * 配置在缓存中的key名
	 */
	@ApiModelProperty(value = "配置在缓存中的key名")
	private String confKey;

	/**
	 * 分类
	 */
	@ApiModelProperty(value = "分类")
	private String category;

}
