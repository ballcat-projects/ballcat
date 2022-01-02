package com.hccake.ballcat.extend.tesseract.exception;

/**
 * @author lingting
 */
public class OcrException extends RuntimeException {

	public OcrException(String message) {
		super(message);
	}

	public OcrException(String message, Throwable cause) {
		super(message, cause);
	}

}
