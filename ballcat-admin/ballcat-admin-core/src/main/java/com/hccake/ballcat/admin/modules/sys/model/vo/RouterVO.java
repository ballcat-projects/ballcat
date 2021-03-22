package com.hccake.ballcat.admin.modules.sys.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 前端路由视图对象
 *
 * @author hccake
 * @date 2021-03-22
 */
@Data
@ApiModel(value = "前端路由视图对象")
public class RouterVO {

	/**
	 * 菜单ID
	 */
	@ApiModelProperty(value = "菜单id")
	private Integer id;

	/**
	 * 父菜单ID
	 */
	@ApiModelProperty(value = "父菜单id")
	private Integer parentId;

	/**
	 * 菜单图标
	 */
	@ApiModelProperty(value = "图标")
	private String icon;

	/**
	 * 菜单名称
	 */
	@ApiModelProperty(value = "标题")
	private String title;

	/**
	 * 前端路由标识路径
	 */
	@ApiModelProperty(value = "前端路由名称")
	private String routerName;

	/**
	 * 前端路由标识路径
	 */
	@ApiModelProperty(value = "前端路由标识路径")
	private String path;

	/**
	 * 菜单权限标识
	 */
	@ApiModelProperty(value = "前端路由组件")
	private String component;

	/**
	 * 重定向地址
	 */
	@ApiModelProperty(value = "重定向地址")
	private String redirect;

	/**
	 * 链接跳转目标
	 */
	@ApiModelProperty(value = "链接跳转目标")
	private String target;

	/**
	 * 路由缓冲
	 */
	@ApiModelProperty(value = "开启路由缓冲")
	private Integer keepAlive;

	/**
	 * 是否隐藏路由: 0否,1是
	 */
	@ApiModelProperty(value = "是否隐藏路由: 0否,1是")
	private Integer hidden;

}
