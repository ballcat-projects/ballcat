package com.hccake.starter.file.local;

import java.io.IOException;

/**
 * @author lingting 2021/10/19 22:26
 */
public class FileLocalException extends IOException {

	public FileLocalException() {
	}

	public FileLocalException(String message) {
		super(message);
	}

	public FileLocalException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileLocalException(Throwable cause) {
		super(cause);
	}

}
