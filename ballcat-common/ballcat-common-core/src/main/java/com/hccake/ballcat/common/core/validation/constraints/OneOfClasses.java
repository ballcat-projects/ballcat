package com.hccake.ballcat.common.core.validation.constraints;

import com.hccake.ballcat.common.core.validation.validator.EnumValueValidatorOfClass;

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
@Repeatable(OneOfClasses.List.class)
@Documented
@Constraint(validatedBy = { EnumValueValidatorOfClass.class })
public @interface OneOfClasses {

	String message() default "value must match one of the values in the list: {value}";

	Class<?>[] value();

	/**
	 * 允许值为 null, 默认不允许
	 */
	boolean allowNull() default false;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		OneOfClasses[] value();

	}

}
