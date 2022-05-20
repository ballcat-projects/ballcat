package com.hccake.ballcat.autoconfigure.i18n;

import com.hccake.ballcat.common.core.validation.EmptyCurlyToDefaultMessageInterpolator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;

import javax.validation.MessageInterpolator;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

/**
 * 支持国际化的校验配置，order 为最高优先级，用于覆盖 starter-web 中的配置
 *
 * @author hccake
 */
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@AutoConfiguration(before = ValidationAutoConfiguration.class, after = I18nMessageSourceAutoConfiguration.class)
@ConditionalOnClass(ExecutableValidator.class)
@ConditionalOnResource(resources = "classpath:META-INF/services/javax.validation.spi.ValidationProvider")
public class I18nValidationConfiguration {

	@Bean
	@ConditionalOnBean(MessageSource.class)
	@ConditionalOnMissingBean({ Validator.class, MessageInterpolator.class })
	public EmptyCurlyToDefaultMessageInterpolator messageInterpolator(MessageSource messageSource) {
		return new EmptyCurlyToDefaultMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource));
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@ConditionalOnMissingBean(Validator.class)
	@ConditionalOnBean(MessageInterpolator.class)
	public static LocalValidatorFactoryBean defaultValidator(MessageInterpolator messageInterpolator) {
		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
		factoryBean.setMessageInterpolator(messageInterpolator);
		return factoryBean;
	}

}
