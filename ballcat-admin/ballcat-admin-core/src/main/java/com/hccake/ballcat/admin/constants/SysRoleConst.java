package com.hccake.ballcat.admin.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hccake 2020/7/6
 * @version 1.0
 */
public final class SysRoleConst {

	/**
	 * 角色类型，1系统角色，2 业务角色
	 */
	@Getter
	@AllArgsConstructor
	public enum Type {

		/**
		 * 系统角色
		 */
		SYSTEM(1),
		/**
		 * 业务角色
		 */
		BUSINESS(2);

		private final Integer value;

	}

}
