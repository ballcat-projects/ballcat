package com.hccake.ballcat.common.core.validation.constraints;

import com.hccake.ballcat.common.core.validation.validator.EnumClassValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author housl
 * @version 1.0
 */
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(EnumClass.List.class)
@Documented
@Constraint(validatedBy = { EnumClassValidator.class })
public @interface EnumClass {

	String message() default "Not in the specified range";

	Class<?>[] targetEnum();

	String method();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		EnumClass[] value();

	}

}
