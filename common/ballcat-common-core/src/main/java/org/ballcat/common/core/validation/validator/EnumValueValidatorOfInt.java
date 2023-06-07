/*
 * Copyright 2023 the original author or authors.
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

import org.ballcat.common.core.validation.constraints.OneOfInts;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 枚举值 Validator
 *
 * @author housl
 */
public class EnumValueValidatorOfInt implements ConstraintValidator<OneOfInts, Integer> {

	private int[] ints;

	private boolean allowNull;

	@Override
	public void initialize(OneOfInts constraintAnnotation) {
		ints = constraintAnnotation.value();
		allowNull = constraintAnnotation.allowNull();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value == null) {
			return allowNull;
		}
		for (int anInt : ints) {
			if (anInt == value) {
				return true;
			}
		}
		return false;
	}

}
