package com.ballcat.core.validation;

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

public class CarTest {

	private static Validator validator;

	@BeforeAll
	public static void setUpValidator() {
		HibernateValidatorConfiguration configure = Validation.byProvider(HibernateValidator.class).configure();
		configure.failFast(true).defaultLocale(Locale.US);
		ValidatorFactory factory = configure.buildValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void manufacturerIsNull() {
		Car car = new Car(null, "DD-AB-123", 4);

		Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

		assertEquals(1, constraintViolations.size());
		assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
	}

	@Test
	void licensePlateTooShort() {
		Car car = new Car("Morris", "D", 4);

		Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

		assertEquals(1, constraintViolations.size());
		assertEquals("size must be between 2 and 14", constraintViolations.iterator().next().getMessage());
	}

	@Test
	void seatCountTooLow() {
		Car car = new Car("Morris", "DD-AB-123", 1);

		Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

		assertEquals(1, constraintViolations.size());
		assertEquals("must be greater than or equal to 2", constraintViolations.iterator().next().getMessage());
	}

	@Test
	void carIsValid() {
		Car car = new Car("Morris", "DD-AB-123", 2);

		Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

		assertEquals(0, constraintViolations.size());
	}

}