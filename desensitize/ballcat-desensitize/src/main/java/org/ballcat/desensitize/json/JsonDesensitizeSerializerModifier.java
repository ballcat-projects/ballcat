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

package org.ballcat.desensitize.json;

import java.lang.annotation.Annotation;
import java.util.List;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.ballcat.desensitize.annotation.AnnotationHandlerHolder;

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
				beanProperty.assignSerializer(new JsonDesensitizeSerializer(annotation, this.desensitizeStrategy));
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
