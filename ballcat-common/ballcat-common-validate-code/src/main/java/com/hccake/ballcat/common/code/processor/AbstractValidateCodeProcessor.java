package com.hccake.ballcat.common.code.processor;

import com.hccake.ballcat.common.code.ValidateCode;
import com.hccake.ballcat.common.code.ValidateCodeGenerator;
import com.hccake.ballcat.common.code.ValidateCodeRepository;
import lombok.Setter;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * AbstractValidateCodeProcessor
 *
 * @author xm.z
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

	@Setter
	private ValidateCodeGenerator validateCodeGenerator;

	@Setter
	private ValidateCodeRepository validateCodeRepository;

	@Override
	public void create(ServletWebRequest request) throws Exception {
		C validateCode = (C) validateCodeGenerator.generate(request);
		save(request, validateCode);
		send(request, validateCode);
	}

	private void save(ServletWebRequest request, C validateCode) {
		ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
		validateCodeRepository.save(request, code);
	}

	/**
	 * Send check code, the specific logic is implemented by the subclass
	 *
	 * @param request      the request
	 * @param validateCode the verification code type
	 * @throws Exception the exception
	 */
	protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

}
