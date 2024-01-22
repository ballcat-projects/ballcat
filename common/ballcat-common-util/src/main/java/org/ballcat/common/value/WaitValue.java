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

package org.ballcat.common.value;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import lombok.Getter;
import org.ballcat.common.lock.JavaReentrantLock;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2023-05-21 20:13
 */
public class WaitValue<T> {

	protected final JavaReentrantLock lock = new JavaReentrantLock();

	@Getter
	protected T value;

	public static <T> WaitValue<T> of() {
		return new WaitValue<>();
	}

	public static <T> WaitValue<T> of(T t) {
		WaitValue<T> of = of();
		of.value = t;
		return of;
	}

	public void update(T t) throws InterruptedException {
		update(v -> t);
	}

	public void update(UnaryOperator<T> operator) throws InterruptedException {
		this.lock.runByInterruptibly(() -> {
			this.value = operator.apply(this.value);
			this.lock.signalAll();
		});
	}

	public T notNull() throws InterruptedException {
		return wait(Objects::nonNull);
	}

	public T notEmpty() throws InterruptedException {
		return wait(v -> {
			if (v == null) {
				return false;
			}

			if (v instanceof Collection) {
				return !CollectionUtils.isEmpty((Collection<?>) v);
			}
			else if (v instanceof Map) {
				return !CollectionUtils.isEmpty((Map<?, ?>) v);
			}
			else if (v instanceof String) {
				return StringUtils.hasText((CharSequence) v);
			}

			return true;
		});
	}

	public T wait(Predicate<T> predicate) throws InterruptedException {
		this.lock.lockInterruptibly();
		try {
			while (true) {
				if (predicate.test(this.value)) {
					return this.value;
				}

				this.lock.await(1, TimeUnit.HOURS);
			}
		}
		finally {
			this.lock.unlock();
		}
	}

}
