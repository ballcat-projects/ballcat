package com.hccake.ballcat.common.code.impl;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.code.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * AbstractValidateCodeProcessor
 *
 * @author xm.z
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

	/**
	 * Collect all the {@link ValidateCodeGenerator} interface implementations in the
	 * system
	 */
	@Autowired
	private Map<String, ValidateCodeGenerator> validateCodeGenerators;

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private ValidateCodeRepository validateCodeRepository;

	@Autowired
	private ValidateCodeInputErrorProcessor validateCodeInputErrorProcessor;

	@Override
	public void create(ServletWebRequest request) throws Exception {
		C validateCode = generate(request);
		save(request, validateCode);
		send(request, validateCode);
	}

	private C generate(ServletWebRequest request) {
		String type = getValidateCodeType(request).toString().toLowerCase();
		String generatorName = type + ValidateCodeGenerator.class.getSimpleName();
		ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(generatorName);
		if (validateCodeGenerator == null) {
			throw new ValidateCodeException("验证码生成器" + generatorName + "不存在");
		}
		return (C) validateCodeGenerator.generate(request);
	}

	private void save(ServletWebRequest request, C validateCode) {
		ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
		validateCodeRepository.save(request, code, getValidateCodeType(request));
	}

	/**
	 * Send check code, the specific logic is implemented by the subclass
	 * @param request the request
	 * @param validateCode the verification code type
	 * @throws Exception the exception
	 */
	protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

	/**
	 * Get the type of verification code according to the requested url
	 * @param request the request
	 * @return {@link ValidateCodeType}
	 */
	private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
		String type = StrUtil.subBefore(getClass().getSimpleName(), "CodeProcessor", true);
		return ValidateCodeType.valueOf(type.toUpperCase());
	}

	@Override
	public void validate(ServletWebRequest request) {

		ValidateCodeType codeType = getValidateCodeType(request);

		C codeInSession = (C) validateCodeRepository.get(request, codeType);

		String codeInRequest;
		try {
			codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
					codeType.getParamNameOnValidate());
		}
		catch (ServletRequestBindingException e) {
			throw new ValidateCodeException(codeType + "获取验证码的值失败");
		}

		if (StrUtil.isBlank(codeInRequest)) {
			throw new ValidateCodeException(codeType + "验证码的值不能为空");
		}

		validateCodeInputErrorProcessor.upperLimitProcess(request, codeType);

		if (codeInSession == null) {
			throw new ValidateCodeException("验证码不存在，请重新获取");
		}

		if (LocalDateTime.now().isAfter(codeInSession.getExpireTime())) {
			validateCodeRepository.remove(request, codeType);
			throw new ValidateCodeException("验证码已过期，请重新获取");
		}

		if (!StrUtil.equals(codeInSession.getCode(), codeInRequest)) {
			validateCodeInputErrorProcessor.errorCount(request, codeType);
			throw new ValidateCodeException("验证码输入错误");
		}

		validateCodeRepository.remove(request, codeType);
	}

}
