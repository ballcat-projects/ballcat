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

package org.ballcat.common.util;

import java.util.function.Supplier;

/**
 * 定制Assert 抛BusinessException
 *
 * @author <a href="mailto:cs.liaow@gmail.com">evil0th</a> Create on 2023/6/7
 */
public final class Assert {

	private Assert() {
	}

	/**
	 * Assert a boolean expression, throwing an {@code Throwable}
	 * @param <X> Throwable
	 * @param expression expression a boolean expression
	 * @param supplier supplier
	 * @throws X if expression is {@code false}
	 */
	public static <X extends Throwable> void isTrue(boolean expression, Supplier<? extends X> supplier) throws X {
		if (!expression) {
			throw supplier.get();
		}
	}

	/**
	 * Assert not null expression, throwing an {@code Throwable}
	 * @param <X> Throwable
	 * @param object a object
	 * @param errorSupplier errorSupplier
	 * @throws X if expression is {@code false}
	 */
	public static <T, X extends Throwable> T notNull(T object, Supplier<X> errorSupplier) throws X {
		if (null == object) {
			throw errorSupplier.get();
		}
		return object;
	}

}
