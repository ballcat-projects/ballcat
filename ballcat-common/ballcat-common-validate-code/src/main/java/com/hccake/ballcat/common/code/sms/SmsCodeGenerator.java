package com.hccake.ballcat.common.code.sms;

import cn.hutool.core.util.RandomUtil;
import com.hccake.ballcat.common.code.ValidateCode;
import com.hccake.ballcat.common.code.ValidateCodeGenerator;
import com.hccake.ballcat.common.code.ValidateCodeProperties;
import lombok.Setter;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * SmsCodeGenerator
 *
 * @author xm.z
 */
public class SmsCodeGenerator implements ValidateCodeGenerator {

	@Setter
	private ValidateCodeProperties.SmsCodeProperties smsCodeProperties;

	@Override
	public ValidateCode generate(ServletWebRequest request) {
		String code = RandomUtil.randomNumbers(smsCodeProperties.getLength());
		return new ValidateCode(code, smsCodeProperties.getExpireIn());
	}

}