package com.hccake.ballcat.common.code.sms;

import cn.hutool.extra.servlet.ServletUtil;
import com.hccake.ballcat.common.code.ValidateCode;
import com.hccake.ballcat.common.code.impl.AbstractValidateCodeProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;
import java.util.Map;

import static com.hccake.ballcat.common.code.constant.CodeConstants.DEFAULT_PARAMETER_NAME_MOBILE;

/**
 * SmsCodeProcessor
 *
 * @author xm.z
 */
@Component("smsValidateCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

	@Resource
	private SmsCodeSender smsCodeSender;

	@Override
	protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
		String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(),
				DEFAULT_PARAMETER_NAME_MOBILE);
		Map<String, String> extra = ServletUtil.getParamMap(request.getRequest());
		extra.remove(DEFAULT_PARAMETER_NAME_MOBILE);
		smsCodeSender.send(mobile, validateCode.getCode(), extra);
	}

}