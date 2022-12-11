package com.hccake.common.core.test.desensite;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author chengbohua
 */
public final class ReflectionUtils {

	private ReflectionUtils() {
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
