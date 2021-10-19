package com.hccake.starter.file;

import java.io.IOException;

/**
 * @author lingting 2021/10/19 22:26
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
