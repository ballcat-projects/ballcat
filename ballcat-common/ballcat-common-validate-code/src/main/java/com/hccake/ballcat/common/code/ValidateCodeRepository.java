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
	 */
	void save(ServletWebRequest request, ValidateCode code);

	/**
	 * get verification code
	 * @param request the request
	 * @return {@link ValidateCode}
	 */
	ValidateCode get(ServletWebRequest request);

	/**
	 * remove verification code
	 * @param request the request
	 */
	void remove(ServletWebRequest request);

}