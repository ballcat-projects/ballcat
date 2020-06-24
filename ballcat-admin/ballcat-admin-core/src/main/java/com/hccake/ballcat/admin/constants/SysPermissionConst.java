package com.hccake.ballcat.admin.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/12 19:01
 */
public final class SysPermissionConst {

	private SysPermissionConst() {
	}

	@Getter
	@AllArgsConstructor
	public enum Type {

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

}
