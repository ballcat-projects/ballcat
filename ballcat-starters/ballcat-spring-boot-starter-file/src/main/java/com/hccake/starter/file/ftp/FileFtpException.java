package com.hccake.starter.file.ftp;

import java.io.IOException;

/**
 * ftp 异常
 *
 * @author lingting 2021/10/18 16:43
 */
public class FileFtpException extends IOException {

	public FileFtpException() {
	}

	public FileFtpException(String message) {
		super(message);
	}

	public FileFtpException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileFtpException(Throwable cause) {
		super(cause);
	}

}
