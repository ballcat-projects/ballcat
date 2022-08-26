package com.hccake.ballcat.extend.tesseract.exception;

/**
 * <p>
 * OcrException class.
 * </p>
 *
 * @author lingting
 */
public class OcrException extends RuntimeException {

	/**
	 * <p>
	 * Constructor for OcrException.
	 * </p>
	 * @param message a {@link java.lang.String} object.
	 */
	public OcrException(String message) {
		super(message);
	}

	/**
	 * <p>
	 * Constructor for OcrException.
	 * </p>
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public OcrException(String message, Throwable cause) {
		super(message, cause);
	}

}
