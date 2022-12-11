package com.hccake.common.core.test.desensite;

import com.hccake.ballcat.common.desensitize.AnnotationHandlerHolder;
import com.hccake.ballcat.common.desensitize.DesensitizationHandlerHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
