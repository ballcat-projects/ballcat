/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.autoconfigure.web.validation;

import javax.validation.MessageInterpolator;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

import org.ballcat.common.core.validation.EmptyCurlyToDefaultMessageInterpolator;
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
