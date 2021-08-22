package com.hccake.ballcat.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统菜单类型
 * @author cheng
 */
@Getter
@AllArgsConstructor
public enum SysMenuType {

	/**
	 * 目录
	 */
	DIRECTORY(0),
	/**
	 * 菜单
	 */
	MENU(1),
	/**
	 * 按钮/权限
	 */
	BUTTON(2);

	private final int value;

}