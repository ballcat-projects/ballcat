package com.hccake.ballcat.common.core.validation.validator;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.core.validation.constraints.EnumClass;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author housl
 * @version 1.0
 * @description 枚举类 Validator
 */
@Slf4j
public class EnumClassValidator implements ConstraintValidator<EnumClass, Object> {

	private Class<?>[] targetEnum;

	private String checkMethod;

	@Override
	public void initialize(EnumClass constraintAnnotation) {
		targetEnum = constraintAnnotation.targetEnum();
		checkMethod = constraintAnnotation.method();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		try {
			if (value == null || StrUtil.isBlank(checkMethod)) {
				return true;
			}
			for (Class<?> eClass : targetEnum) {
				if (eClass.isEnum()) {
					Method method = eClass.getDeclaredMethod(checkMethod, value.getClass());
					return (Boolean) method.invoke(null, value);
				}
				else {
					return false;
				}
			}
		}
		catch (NoSuchMethodException e) {
			log.error("No corresponding static check method!");
			return false;
		}
		catch (InvocationTargetException | IllegalAccessException e) {
			return false;
		}
		return false;
	}

}
