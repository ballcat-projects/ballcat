package com.hccake.ballcat.common.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * ValidateCodeInputErrorProcessor
 *
 * @author xm.z
 */
public interface ValidateCodeInputErrorProcessor {

	/**
	 * 错误计次
	 * @param request the current request attributes
	 * @param codeType the verification code type
	 * @throws ValidateCodeException the exception
	 */
	void errorCount(ServletWebRequest request, ValidateCodeType codeType) throws ValidateCodeException;

	/**
	 * 错误上限处理
	 * @param request the current request attributes
	 * @param codeType the verification code type
	 * @throws ValidateCodeException the exception
	 */
	void upperLimitProcess(ServletWebRequest request, ValidateCodeType codeType) throws ValidateCodeException;

}
