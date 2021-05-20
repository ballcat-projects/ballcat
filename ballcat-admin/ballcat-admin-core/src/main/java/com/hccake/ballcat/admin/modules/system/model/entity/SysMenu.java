package com.hccake.ballcat.admin.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 菜单权限
 *
 * @author hccake 2021-04-06 17:59:51
 */
@Data
@TableName("sys_menu")
@ApiModel(value = "菜单权限")
public class SysMenu {

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单ID
	 */
	@TableId(type = IdType.INPUT)
	@ApiModelProperty(value = "菜单ID")
	private Integer id;

	/**
	 * 父级ID
	 */
	@ApiModelProperty(value = "父级ID")
	private Integer parentId;

	/**
	 * 菜单名称
	 */
	@ApiModelProperty(value = "菜单名称")
	private String title;

	/**
	 * 菜单图标
	 */
	@ApiModelProperty(value = "菜单图标")
	private String icon;

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

	/**
	 * 打开方式 (1组件 2内链 3外链)
	 */
	@ApiModelProperty(value = "打开方式 (1组件 2内链 3外链)")
	private Integer targetType;

	/**
	 * 定位标识 (打开方式为组件时其值为组件相对路径，其他为URL地址)
	 */
	@ApiModelProperty(value = "定位标识 (打开方式为组件时其值为组件相对路径，其他为URL地址)")
	private String uri;

	/**
	 * 显示排序
	 */
	@ApiModelProperty(value = "显示排序")
	private Integer sort;

	/**
	 * 组件缓存：0-开启，1-关闭
	 */
	@ApiModelProperty(value = "组件缓存：0-开启，1-关闭")
	private Integer keepAlive;

	/**
	 * 隐藏菜单: 0-否，1-是
	 */
	@ApiModelProperty(value = "隐藏菜单:  0-否，1-是")
	private Integer hidden;

	/**
	 * 菜单类型 （0目录，1菜单，2按钮）
	 */
	@ApiModelProperty(value = "菜单类型 （0目录，1菜单，2按钮）")
	private Integer type;

	/**
	 * 备注信息
	 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;

	/**
	 * 逻辑删除标识，未删除为 0，已删除为删除时间
	 */
	@TableLogic
	@ApiModelProperty(value = "逻辑删除标识，未删除为 0，已删除为删除时间")
	private Long deleted;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

}
