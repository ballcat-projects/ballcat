package com.hccake.ballcat.extend.tesseract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * TesseractLang class.
 * </p>
 *
 * @author lingting
 * @version $Id: $Id
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
