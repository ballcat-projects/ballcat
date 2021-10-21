package com.hccake.starter.file.ftp;

import java.io.IOException;

/**
 * ftp 异常
 *
 * @author lingting 2021/10/18 16:43
 */
public class FtpFileException extends IOException {

	public FtpFileException() {
	}

	public FtpFileException(String message) {
		super(message);
	}

	public FtpFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public FtpFileException(Throwable cause) {
		super(cause);
	}

}
