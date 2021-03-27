package com.hccake.ballcat.common.desensitize.json;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.hccake.ballcat.common.desensitize.AnnotationHandlerHolder;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonRegexDesensitize;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonSimpleDesensitize;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonSlideDesensitize;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * json serial modifier
 *
 * @author Yakir
 */
public class JsonSerializerModifier extends BeanSerializerModifier {

	private DesensitizeHandler desensitizeHandler;

	public JsonSerializerModifier() {
	}

	public JsonSerializerModifier(DesensitizeHandler desensitizeHandler) {
		this.desensitizeHandler = desensitizeHandler;
	}

	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
			List<BeanPropertyWriter> beanProperties) {
		for (BeanPropertyWriter beanProperty : beanProperties) {
			Annotation annotation = getDesensitizeAnnotation(beanProperty);

			if (annotation != null && beanProperty.getType().isTypeOrSubTypeOf(String.class)) {
				beanProperty.assignSerializer(new JsonDesensitizeSerializer(annotation, desensitizeHandler));
			}
		}
		return beanProperties;
	}

	/**
	 * 得到脱敏注解
	 * @param beanProperty
	 * @return 返回第一个获取的脱敏注解
	 */
	private Annotation getDesensitizeAnnotation(BeanPropertyWriter beanProperty) {
		for (Class<? extends Annotation> annotationClass : AnnotationHandlerHolder.getAnnotationClasses()) {
			Annotation annotation = beanProperty.getAnnotation(annotationClass);
			if (annotation != null) {
				return annotation;
			}
		}
		return null;
	}

}
