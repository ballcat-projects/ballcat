package com.hccake.ballcat.system.model.qo;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

/**
 * 菜单权限 查询对象
 *
 * @author hccake 2021-04-06 17:59:51
 */
@Data
@Schema(title = "菜单权限查询对象")
@ParameterObject
public class SysMenuQO {

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单ID
	 */
	@Parameter(description = "菜单ID")
	private Integer id;

	/**
	 * 菜单名称
	 */
	@Parameter(description = "菜单名称")
	private String title;

	/**
	 * 授权标识
	 */
	@Parameter(description = "授权标识")
	private String permission;

	/**
	 * 路由地址
	 */
	@Parameter(description = "路由地址")
	private String path;

}