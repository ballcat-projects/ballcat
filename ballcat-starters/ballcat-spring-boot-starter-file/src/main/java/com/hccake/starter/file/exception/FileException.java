package com.hccake.starter.file.exception;

import java.io.IOException;

/**
 * 文件系统异常
 *
 * @author 疯狂的狮子Li 2022-04-24
 */
public class FileException extends IOException {

	public FileException() {
	}

	public FileException(String message) {
		super(message);
	}

	public FileException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileException(Throwable cause) {
		super(cause);
	}

}
