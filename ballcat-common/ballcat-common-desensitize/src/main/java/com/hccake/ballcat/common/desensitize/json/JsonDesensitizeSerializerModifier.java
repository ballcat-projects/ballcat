package com.hccake.ballcat.common.desensitize.json;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.hccake.ballcat.common.desensitize.AnnotationHandlerHolder;

import java.lang.annotation.Annotation;

import java.util.List;

/**
 * json serial modifier
 *
 * @author Yakir
 */
public class JsonDesensitizeSerializerModifier extends BeanSerializerModifier {

	private DesensitizeStrategy desensitizeStrategy;

	public JsonDesensitizeSerializerModifier() {
	}

	public JsonDesensitizeSerializerModifier(DesensitizeStrategy desensitizeStrategy) {
		this.desensitizeStrategy = desensitizeStrategy;
	}

	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
			List<BeanPropertyWriter> beanProperties) {
		for (BeanPropertyWriter beanProperty : beanProperties) {
			Annotation annotation = getDesensitizeAnnotation(beanProperty);

			if (annotation != null && beanProperty.getType().isTypeOrSubTypeOf(String.class)) {
				beanProperty.assignSerializer(new JsonDesensitizeSerializer(annotation, desensitizeStrategy));
			}
		}
		return beanProperties;
	}

	/**
	 * 得到脱敏注解
	 * @param beanProperty BeanPropertyWriter
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
