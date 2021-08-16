package com.hccake.ballcat.common.core.validation;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * 将消息中空的花括号替换为校验注解的默认值
 *
 * 扩展自原有的 {@link ResourceBundleMessageInterpolator} 消息处理器
 *
 * @author hccake
 */
public class EmptyCurlyToDefaultMessageInterpolator extends ResourceBundleMessageInterpolator {

	private static final String EMPTY_CURLY_BRACES = "{}";

	private static final String LEFT_CURLY_BRACES = "{";

	private static final String RIGHT_CURLY_BRACES = "}";

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
			Class<? extends Annotation> annotationType = context.getConstraintDescriptor().getAnnotation()
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
					if (defaultMessage.startsWith(LEFT_CURLY_BRACES) && defaultMessage.endsWith(RIGHT_CURLY_BRACES)) {
						message = message.replace(EMPTY_CURLY_BRACES, defaultMessage);
					}
				}
			}
		}

		return super.interpolate(message, context, locale);
	}

}
