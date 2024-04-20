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

package org.ballcat.desensite;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.ballcat.desensitize.DesensitizationHandlerHolder;
import org.ballcat.desensitize.annotation.AnnotationHandlerHolder;

/**
 * @author chengbohua
 */
public final class TestUtils {

	private TestUtils() {
	}

	public static void resetEnv() throws Exception {
		// 初始化，防止其他测试用例修改了脱敏处理器
		Field instanceField = DesensitizationHandlerHolder.class.getDeclaredField("INSTANCE");
		DesensitizationHandlerHolder newInstance = TestUtils.getNewInstance(DesensitizationHandlerHolder.class);
		TestUtils.setFinalStaticField(instanceField, newInstance);

		Field annotationHolderHandlerInstanceFiled = AnnotationHandlerHolder.class.getDeclaredField("INSTANCE");
		AnnotationHandlerHolder annotationHandlerHolder = TestUtils.getNewInstance(AnnotationHandlerHolder.class);
		TestUtils.setFinalStaticField(annotationHolderHandlerInstanceFiled, annotationHandlerHolder);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getNewInstance(Class<T> clazz) throws Exception {
		Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
		if (!declaredConstructor.isAccessible()) {
			declaredConstructor.setAccessible(true);
		}
		return (T) declaredConstructor.newInstance();
	}

	public static void setFinalStaticField(Field field, Object newValue) throws Exception {
		field.setAccessible(true);

		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, newValue);
	}

}
