package com.hccake.ballcat.common.core.validation.validator;

import com.hccake.ballcat.common.core.validation.constraints.EnumValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author housl
 * @version 1.0
 * @description 枚举值 Validator
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

	private int[] intArr;

	private String[] strArr;

	@Override
	public void initialize(EnumValue constraintAnnotation) {
		intArr = constraintAnnotation.intValues();
		strArr = constraintAnnotation.strValues();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		if (value instanceof Integer) {
			for (Integer intValue : intArr) {
				if (intValue == value) {
					return true;
				}
			}
		}
		else if (value instanceof String) {
			for (String strValue : strArr) {
				if (strValue.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

}
