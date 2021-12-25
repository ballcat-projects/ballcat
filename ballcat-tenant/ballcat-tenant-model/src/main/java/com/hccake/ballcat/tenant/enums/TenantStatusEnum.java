package com.hccake.ballcat.tenant.enums;

/**
 * @author huyuanzhi
 */

public enum TenantStatusEnum {

	/**
	 * 正常
	 */
	ENABLE(1),
	/**
	 * 禁用
	 */
	DISABLE(0);

	private final int status;

	TenantStatusEnum(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

}
