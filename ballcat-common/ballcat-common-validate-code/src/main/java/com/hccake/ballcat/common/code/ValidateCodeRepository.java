package com.hccake.ballcat.common.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * ValidateCodeRepository
 *
 * @author xm.z
 */
public interface ValidateCodeRepository {

	/**
	 * Save verification code
	 * @param request the request
	 * @param code the verification code
	 * @param codeType the verification code type
	 */
	void save(ServletWebRequest request, ValidateCode code, ValidateCodeType codeType);

	/**
	 * get verification code
	 * @param request the request
	 * @param codeType the verification code type
	 * @return {@link ValidateCode}
	 */
	ValidateCode get(ServletWebRequest request, ValidateCodeType codeType);

	/**
	 * remove verification code
	 * @param request the request
	 * @param codeType the verification code type
	 */
	void remove(ServletWebRequest request, ValidateCodeType codeType);

}