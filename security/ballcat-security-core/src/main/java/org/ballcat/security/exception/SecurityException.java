package org.ballcat.security.exception;

/**
 * @author lingting 2023-03-29 20:57
 */
public abstract class SecurityException extends RuntimeException {

	protected SecurityException() {
		super();
	}

	protected SecurityException(String message) {
		super(message);
	}

	protected SecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	protected SecurityException(Throwable cause) {
		super(cause);
	}

	protected SecurityException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
