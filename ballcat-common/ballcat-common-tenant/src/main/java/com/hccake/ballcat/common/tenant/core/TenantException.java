package com.hccake.ballcat.common.tenant.core;

/**
 * @author huyuanzhi
 */
public class TenantException extends RuntimeException {

	public TenantException(String message) {
		super(message);
	}

	public TenantException(String message, Throwable cause) {
		super(message, cause);
	}

}
