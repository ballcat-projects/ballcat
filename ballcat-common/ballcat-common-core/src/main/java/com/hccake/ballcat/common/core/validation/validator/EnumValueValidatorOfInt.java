package com.hccake.ballcat.common.core.validation.validator;

import com.hccake.ballcat.common.core.validation.constraints.OneOfInts;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author housl
 * @version 1.0 枚举值 Validator
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
