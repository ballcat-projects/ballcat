package com.hccake.ballcat.common.core.validation.validator;

import com.hccake.ballcat.common.core.validation.constraints.OneOfStrings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author housl
 * @version 1.0 枚举值 Validator
 */
public class EnumValueValidatorOfString implements ConstraintValidator<OneOfStrings, String> {

	private String[] stringList;

	private boolean allowNull;

	@Override
	public void initialize(OneOfStrings constraintAnnotation) {
		stringList = constraintAnnotation.value();
		allowNull = constraintAnnotation.allowNull();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return allowNull;
		}
		for (String strValue : stringList) {
			if (strValue.equals(value)) {
				return true;
			}
		}
		return false;
	}

}
