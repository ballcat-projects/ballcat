package com.hccake.ballcat.common.tenant.core;

/**
 * @author huyuanzhi
 */

public enum TenantType {

	/**
	 * 非租户模式
	 */
	NONE,
	/**
	 * 列模式
	 */
	COLUMN,
	/**
	 * 单独db模式
	 */
	DATASOURCE,
	/**
	 * 混合模式，COLUMN+DATASOURCE
	 */
	MIX

}
