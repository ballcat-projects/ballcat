package com.hccake.ballcat.autoconfigure.web.validation;

import com.hccake.ballcat.common.core.validation.EmptyCurlyToDefaultMessageInterpolator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.MessageInterpolator;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

/**
 * Validation 自动配置类，扩展支持使用 {} 占位替换默认消息
 *
 * @author hccake
 */
@AutoConfiguration(before = ValidationAutoConfiguration.class)
@ConditionalOnClass(ExecutableValidator.class)
@ConditionalOnResource(resources = "classpath:META-INF/services/javax.validation.spi.ValidationProvider")
public class BallcatValidationAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean({ Validator.class, MessageInterpolator.class })
	public EmptyCurlyToDefaultMessageInterpolator messageInterpolator() {
		return new EmptyCurlyToDefaultMessageInterpolator();
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
