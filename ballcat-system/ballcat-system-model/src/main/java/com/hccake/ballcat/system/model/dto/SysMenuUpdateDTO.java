package com.hccake.ballcat.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 菜单权限
 *
 * @author hccake 2021-04-06 17:59:51
 */
@Data
@Schema(title = "菜单权限修改DTO")
public class SysMenuUpdateDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单ID
	 */
	@NotNull(message = "菜单ID不能为空")
	@Schema(title = "菜单ID")
	private Integer id;

	/**
	 * 父级ID
	 */
	@Schema(title = "父级ID")
	private Integer parentId;

	/**
	 * 菜单名称
	 */
	@Schema(title = "菜单名称")
	private String title;

	/**
	 * 菜单图标
	 */
	@Schema(title = "菜单图标")
	private String icon;

	/**
	 * 授权标识
	 */
	@Schema(title = "授权标识")
	private String permission;

	/**
	 * 路由地址
	 */
	@Schema(title = "路由地址")
	private String path;

	/**
	 * 打开方式 (1组件 2内链 3外链)
	 */
	@Schema(title = "打开方式 (1组件 2内链 3外链)")
	private Integer targetType;

	/**
	 * 定位标识 (打开方式为组件时其值为组件相对路径，其他为URL地址)
	 */
	@Schema(title = "定位标识 (打开方式为组件时其值为组件相对路径，其他为URL地址)")
	private String uri;

	/**
	 * 显示排序
	 */
	@Schema(title = "显示排序")
	private Integer sort;

	/**
	 * 组件缓存：0-开启，1-关闭
	 */
	@Schema(title = "组件缓存：0-开启，1-关闭")
	private Integer keepAlive;

	/**
	 * 隐藏菜单: 0-否，1-是
	 */
	@Schema(title = "隐藏菜单:  0-否，1-是")
	private Integer hidden;

	/**
	 * 菜单类型 （0目录，1菜单，2按钮）
	 */
	@Schema(title = "菜单类型 （0目录，1菜单，2按钮）")
	private Integer type;

	/**
	 * 备注信息
	 */
	@Schema(title = "备注信息")
	private String remarks;

	/**
	 * 原菜单ID
	 */
	@NotNull(message = "原菜单ID不能为空")
	@Schema(title = "原菜单ID")
	private Integer originalId;

}
