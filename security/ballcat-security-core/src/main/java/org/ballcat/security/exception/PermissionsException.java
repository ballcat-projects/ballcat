package org.ballcat.security.exception;

/**
 * 权限异常, 不满足指定访问权限
 *
 * @author lingting 2023-03-29 20:57
 */
public class PermissionsException extends SecurityException {

	public PermissionsException() {
		super();
	}

	public PermissionsException(String s) {
		super(s);
	}

	public PermissionsException(String message, Throwable cause) {
		super(message, cause);
	}

	public PermissionsException(Throwable cause) {
		super(cause);
	}

}
