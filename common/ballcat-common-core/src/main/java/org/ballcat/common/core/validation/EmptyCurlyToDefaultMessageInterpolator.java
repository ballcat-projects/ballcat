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

package org.ballcat.common.core.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Locale;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

/**
 * 将消息中空的花括号替换为校验注解的默认值
 * <p>
 * 扩展自原有的 {@link ResourceBundleMessageInterpolator} 消息处理器
 *
 * @author hccake
 */
public class EmptyCurlyToDefaultMessageInterpolator extends ResourceBundleMessageInterpolator {

	private static final String EMPTY_CURLY_BRACES = "{}";

	public EmptyCurlyToDefaultMessageInterpolator() {
	}

	public EmptyCurlyToDefaultMessageInterpolator(ResourceBundleLocator userResourceBundleLocator) {
		super(userResourceBundleLocator);
	}

	@Override
	public String interpolate(String message, Context context, Locale locale) {

		// 如果包含花括号占位符
		if (message.contains(EMPTY_CURLY_BRACES)) {
			// 获取注解类型
			Class<? extends Annotation> annotationType = context.getConstraintDescriptor()
				.getAnnotation()
				.annotationType();

			Method messageMethod;
			try {
				messageMethod = annotationType.getDeclaredMethod("message");
			}
			catch (NoSuchMethodException e) {
				return super.interpolate(message, context, locale);
			}

			// 找到对应 message 的默认值，将 {} 替换为默认值
			if (messageMethod.getDefaultValue() != null) {
				Object defaultValue = messageMethod.getDefaultValue();
				if (defaultValue instanceof String) {
					String defaultMessage = (String) defaultValue;
					message = message.replace(EMPTY_CURLY_BRACES, defaultMessage);
				}
			}
		}

		return super.interpolate(message, context, locale);
	}

}
