package com.hccake.ballcat.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hccake.ballcat.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * 菜单权限
 *
 * @author hccake 2021-04-06 17:59:51
 */
@Getter
@Setter
@ToString
@TableName("sys_menu")
@Schema(title = "菜单权限")
public class SysMenu extends LogicDeletedBaseEntity {

	/**
	 * 菜单ID
	 */
	@TableId(type = IdType.INPUT)
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
	@TableField(updateStrategy = FieldStrategy.NOT_NULL)
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysMenu sysMenu = (SysMenu) o;
		return id.equals(sysMenu.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
