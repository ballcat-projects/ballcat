package com.hccake.ballcat.common.code;

import com.hccake.ballcat.common.code.image.ImageCodeGenerator;
import com.hccake.ballcat.common.code.image.ImageCodeProcessor;
import com.hccake.ballcat.common.code.impl.DefaultValidateCodeInputErrorProcessor;
import com.hccake.ballcat.common.code.sms.DefaultSmsCodeSender;
import com.hccake.ballcat.common.code.sms.SmsCodeGenerator;
import com.hccake.ballcat.common.code.sms.SmsCodeSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * Verification code related extension point configuration. You can override the default
 * configuration by declaring beans of the same type or the same name.
 *
 * @author xm.z
 */
@EnableConfigurationProperties(ValidateCodeProperties.class)
public class ValidateCodeBeanConfig {

	@Resource
	private ValidateCodeProperties validateCodeProperties;

	/**
	 * Validate Code Filter
	 * @return ValidateCodeFilter
	 */
	@Bean
	@ConditionalOnMissingBean(ValidateCodeFilter.class)
	public ValidateCodeFilter validateCodeFilter() {
		return new ValidateCodeFilter();
	}

	/**
	 * Validate Code Processor Holder
	 * @return ValidateCodeProcessorHolder
	 */
	@Bean
	public ValidateCodeProcessorHolder validateCodeProcessorHolder() {
		return new ValidateCodeProcessorHolder();
	}

	/**
	 * Image Code Processor
	 * @return ImageCodeProcessor
	 */
	@Bean
	public ImageCodeProcessor imageCodeProcessor() {
		return new ImageCodeProcessor();
	}

	/**
	 * Image Validate Code Generator
	 * @return ValidateCodeGenerator
	 */
	@Bean
	@ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
	public ValidateCodeGenerator imageValidateCodeGenerator() {
		ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
		codeGenerator.setImageCodeProperties(validateCodeProperties.getImage());
		return codeGenerator;
	}

	/**
	 * Sms Validate Code Generator
	 * @return ValidateCodeGenerator
	 */
	@Bean
	@ConditionalOnMissingBean(name = "smsValidateCodeGenerator")
	public ValidateCodeGenerator smsValidateCodeGenerator() {
		SmsCodeGenerator codeGenerator = new SmsCodeGenerator();
		codeGenerator.setSmsCodeProperties(validateCodeProperties.getSms());
		return codeGenerator;
	}

	/**
	 * SMS verification code sender
	 * @return SmsCodeSender
	 */
	@Bean
	@ConditionalOnMissingBean(SmsCodeSender.class)
	public SmsCodeSender smsCodeSender() {
		return new DefaultSmsCodeSender();
	}

	/**
	 * Validate Code Input Error Processor
	 * @return ValidateCodeInputErrorProcessor
	 */
	@Bean
	@ConditionalOnMissingBean(ValidateCodeInputErrorProcessor.class)
	public ValidateCodeInputErrorProcessor validateCodeInputErrorProcessor() {
		return new DefaultValidateCodeInputErrorProcessor();
	}

}