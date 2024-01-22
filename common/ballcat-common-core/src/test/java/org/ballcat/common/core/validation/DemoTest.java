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

package org.ballcat.common.core.validation;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
			log.debug("field: [{}], 校验异常：{}", constraintViolation.getPropertyPath(), constraintViolation.getMessage());
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
					assertEquals("value is not in enum [class " + StatusEnum.class.getName() + "]",
							constraintViolation.getMessage());
					break;
				case "statusName":
					assertEquals("value is not in enum [class " + StatusEnum.class.getName() + "]",
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
