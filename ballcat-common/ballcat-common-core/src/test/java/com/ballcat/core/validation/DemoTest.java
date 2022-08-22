package com.ballcat.core.validation;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author hccake
 */
@Slf4j
public class DemoTest {

	private static Validator validator;

	@BeforeAll
	public static void setUpValidator() {
		HibernateValidatorConfiguration configure = Validation.byProvider(HibernateValidator.class).configure();
		configure.defaultLocale(Locale.US);
		ValidatorFactory factory = configure.buildValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void manufacturerIsNull() {
		Demo demo = new Demo(null, 3, 2, "SUCCESS1");

		Set<ConstraintViolation<Demo>> constraintViolations = validator.validate(demo);

		for (ConstraintViolation<Demo> constraintViolation : constraintViolations) {
			log.error("field: [{}], 校验异常：{}", constraintViolation.getPropertyPath(), constraintViolation.getMessage());
		}

		assertEquals(4, constraintViolations.size());

		for (ConstraintViolation<Demo> constraintViolation : constraintViolations) {

			switch (constraintViolation.getPropertyPath().toString()) {
				case "name":
					assertEquals("value must match one of the values in the list: [张三, 李四]",
							constraintViolation.getMessage());
					break;
				case "statusValue":
					assertEquals("value must match one of the values in the list: [1, 0]",
							constraintViolation.getMessage());
					break;
				case "status":
					assertEquals("value is not in enum [class com.ballcat.core.validation.StatusEnum]",
							constraintViolation.getMessage());
					break;
				case "statusName":
					assertEquals("value is not in enum [class com.ballcat.core.validation.StatusEnum]",
							constraintViolation.getMessage());
					break;
			}
		}

	}

	@Test
	void demoIsValid() {
		Demo demo = new Demo("张三", 0, 1, "SUCCESS");

		Set<ConstraintViolation<Demo>> constraintViolations = validator.validate(demo);
		assertEquals(0, constraintViolations.size());
	}

}
