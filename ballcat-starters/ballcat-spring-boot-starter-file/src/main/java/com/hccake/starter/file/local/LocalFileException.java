package com.hccake.starter.file.local;

import java.io.IOException;

/**
 * @author lingting 2021/10/19 22:26
 */
public class LocalFileException extends IOException {

	public LocalFileException() {
	}

	public LocalFileException(String message) {
		super(message);
	}

	public LocalFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public LocalFileException(Throwable cause) {
		super(cause);
	}

}
