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

package org.ballcat.common.core.validation.validator;

import javax.validation.ConstraintDefinitionException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.ballcat.common.core.validation.constraints.ValueOfEnum;

/**
 * 枚举类 Validator
 *
 * @author housl
 */
@Slf4j
public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, Object> {

	private Class<?>[] targetEnum;

	private String checkMethod;

	private boolean allowNull;

	@Override
	public void initialize(ValueOfEnum constraintAnnotation) {
		this.targetEnum = constraintAnnotation.enumClass();
		this.checkMethod = constraintAnnotation.method();
		this.allowNull = constraintAnnotation.allowNull();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null) {
			return this.allowNull;
		}
		for (Class<?> eClass : this.targetEnum) {
			// 包装类和原始类型的处理
			Class<?> clazz = ClassUtils.isPrimitiveWrapper(value.getClass())
					? ClassUtils.wrapperToPrimitive(value.getClass()) : value.getClass();

			try {
				Object enumInstance = MethodUtils.invokeStaticMethod(eClass, this.checkMethod, new Object[] { value },
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
