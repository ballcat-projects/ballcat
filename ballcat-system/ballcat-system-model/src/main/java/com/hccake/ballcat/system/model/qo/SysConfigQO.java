package com.hccake.ballcat.system.model.qo;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

/**
 * 系统配置表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@Data
@Schema(title = "基础配置")
@ParameterObject
public class SysConfigQO {

	/**
	 * 配置名称
	 */
	@Parameter(description = "配置名称")
	private String name;

	/**
	 * 配置在缓存中的key名
	 */
	@Parameter(description = "配置在缓存中的key名")
	private String confKey;

	/**
	 * 分类
	 */
	@Parameter(description = "分类")
	private String category;

}
