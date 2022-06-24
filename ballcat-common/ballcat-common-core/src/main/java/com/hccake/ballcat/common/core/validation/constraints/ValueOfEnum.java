package com.hccake.ballcat.common.core.validation.constraints;

import com.hccake.ballcat.common.core.validation.validator.ValueOfEnumValidator;

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
@Repeatable(ValueOfEnum.List.class)
@Documented
@Constraint(validatedBy = { ValueOfEnumValidator.class })
public @interface ValueOfEnum {

	String message() default "value is not in enum {enumClass}";

	Class<?>[] enumClass();

	String method() default "valueOf";

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

		ValueOfEnum[] value();

	}

}
