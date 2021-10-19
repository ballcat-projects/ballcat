package com.hccake.starter.ftp;

import java.io.IOException;

/**
 * ftp 异常
 * @author lingting 2021/10/18 16:43
 */
public class FtpException extends IOException {

	public FtpException() {
	}

	public FtpException(String message) {
		super(message);
	}

	public FtpException(String message, Throwable cause) {
		super(message, cause);
	}

	public FtpException(Throwable cause) {
		super(cause);
	}

}
