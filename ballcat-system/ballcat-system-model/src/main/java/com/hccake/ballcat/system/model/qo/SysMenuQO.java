package com.hccake.ballcat.system.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 菜单权限 查询对象
 *
 * @author hccake 2021-04-06 17:59:51
 */
@Data
@ApiModel(value = "菜单权限查询对象")
public class SysMenuQO {

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单ID
	 */
	@ApiModelProperty(value = "菜单ID")
	private Integer id;

	/**
	 * 菜单名称
	 */
	@ApiModelProperty(value = "菜单名称")
	private String title;

	/**
	 * 授权标识
	 */
	@ApiModelProperty(value = "授权标识")
	private String permission;

	/**
	 * 路由地址
	 */
	@ApiModelProperty(value = "路由地址")
	private String path;

}