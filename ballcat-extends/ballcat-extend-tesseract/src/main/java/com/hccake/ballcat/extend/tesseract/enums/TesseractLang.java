package com.hccake.ballcat.extend.tesseract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting
 */
@Getter
@AllArgsConstructor
public enum TesseractLang {

	/**
	 * en
	 */
	EN("eng"),
	/**
	 * zh
	 */
	ZH("chi_sim"),

	;

	private final String val;

}
