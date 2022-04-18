package com.hccake.ballcat.common.code;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * ValidateCodeProcessorHolder
 *
 * @author xm.z
 */
@Component
public class ValidateCodeProcessorHolder {

	@Resource
	private Map<String, ValidateCodeProcessor> validateCodeProcessors;

	/**
	 * Obtain the corresponding verification code processor by the verification code type
	 * @param type Verification code type
	 * @return ValidateCodeProcessor
	 */
	public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
		return findValidateCodeProcessor(type.toString().toLowerCase());
	}

	/**
	 * Obtain the corresponding verification code processor by the verification code type
	 * @param type Verification code type
	 * @return ValidateCodeProcessor
	 */
	public ValidateCodeProcessor findValidateCodeProcessor(String type) {
		String name = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
		ValidateCodeProcessor processor = validateCodeProcessors.get(name);
		if (processor == null) {
			throw new ValidateCodeException("验证码处理器" + name + "不存在");
		}
		return processor;
	}

}
