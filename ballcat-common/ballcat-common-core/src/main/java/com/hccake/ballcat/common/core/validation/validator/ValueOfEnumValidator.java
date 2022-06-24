package com.hccake.ballcat.common.core.validation.validator;

import com.hccake.ballcat.common.core.validation.constraints.ValueOfEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import javax.validation.ConstraintDefinitionException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author housl
 * @version 1.0 枚举类 Validator
 */
@Slf4j
public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, Object> {

	private Class<?>[] targetEnum;

	private String checkMethod;

	private boolean allowNull;

	@Override
	public void initialize(ValueOfEnum constraintAnnotation) {
		targetEnum = constraintAnnotation.enumClass();
		checkMethod = constraintAnnotation.method();
		allowNull = constraintAnnotation.allowNull();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null) {
			return allowNull;
		}
		for (Class<?> eClass : targetEnum) {
			// 包装类和原始类型的处理
			Class<?> clazz = ClassUtils.isPrimitiveWrapper(value.getClass())
					? ClassUtils.wrapperToPrimitive(value.getClass()) : value.getClass();

			try {
				Object enumInstance = MethodUtils.invokeStaticMethod(eClass, checkMethod, new Object[] { value },
						new Class[] { clazz });
				return enumInstance != null;
			}
			catch (NoSuchMethodException ex) {
				throw new ConstraintDefinitionException(ex);
			}
			catch (Exception ex) {
				log.warn("check enum error: ", ex);
				return false;
			}
		}
		return false;
	}

}
