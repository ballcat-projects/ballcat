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
package org.ballcat.easyexcel.kit;

import javax.validation.*;
import java.util.Set;

/**
 * 校验工具
 *
 * @author L.cm
 */
public final class Validators {

	private Validators() {
	}

	private static final Validator VALIDATOR;

	static {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		VALIDATOR = factory.getValidator();
	}

	/**
	 * Validates all constraints on {@code object}.
	 * @param object object to validate
	 * @param <T> the type of the object to validate
	 * @return constraint violations or an empty set if none
	 * @throws IllegalArgumentException if object is {@code null} or if {@code null} is
	 * passed to the varargs groups
	 * @throws ValidationException if a non recoverable error happens during the
	 * validation process
	 */
	public static <T> Set<ConstraintViolation<T>> validate(T object) {
		return VALIDATOR.validate(object);
	}

}
