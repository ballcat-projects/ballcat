package com.hccake.ballcat.common.code.impl;

import com.hccake.ballcat.common.code.ValidateCodeException;
import com.hccake.ballcat.common.code.ValidateCodeInputErrorProcessor;
import com.hccake.ballcat.common.code.ValidateCodeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * DefaultValidateCodeInputErrorProcessor
 *
 * @author xm.z
 */
@Slf4j
public class DefaultValidateCodeInputErrorProcessor implements ValidateCodeInputErrorProcessor {

	@Override
	public void errorCount(ServletWebRequest request, ValidateCodeType codeType) throws ValidateCodeException {
	}

	@Override
	public void upperLimitProcess(ServletWebRequest request, ValidateCodeType codeType) throws ValidateCodeException {
	}

}