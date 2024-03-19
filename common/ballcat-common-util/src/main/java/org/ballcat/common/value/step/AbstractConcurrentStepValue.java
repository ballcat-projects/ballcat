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

package org.ballcat.common.value.step;

import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.SneakyThrows;
import org.ballcat.common.lock.JavaReentrantLock;

/**
 * @author lingting 2024-01-15 19:21
 */
@SuppressWarnings("java:S2272")
public abstract class AbstractConcurrentStepValue<T> extends AbstractStepValue<T> {

	protected final JavaReentrantLock lock = new JavaReentrantLock();

	@Override
	public T firt() {
		return calculate(BigInteger.ZERO);
	}

	@Override
	@SneakyThrows
	public BigInteger index() {
		return this.lock.getByInterruptibly(super::index);
	}

	@Override
	@SneakyThrows
	public void reset() {
		this.lock.runByInterruptibly(super::reset);
	}

	@Override
	@SneakyThrows
	public List<T> values() {
		return this.lock.getByInterruptibly(super::values);
	}

	@Override
	@SneakyThrows
	public T next() {
		return this.lock.getByInterruptibly(() -> {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return doNext();
		});
	}

	@Override
	@SneakyThrows
	public BigInteger increasing() {
		return this.lock.getByInterruptibly(super::increasing);
	}

	@Override
	@SneakyThrows
	public T calculateNext() {
		return this.lock.getByInterruptibly(super::calculateNext);
	}

	@Override
	@SneakyThrows
	public T calculate(BigInteger calculateIndex) {
		return this.lock.getByInterruptibly(() -> doCalculate(calculateIndex));
	}

	@Override
	@SneakyThrows
	public boolean hasNext() {
		return this.lock.getByInterruptibly(this::doHasNext);
	}

	public abstract boolean doHasNext();

	public T doNext() {
		return super.next();
	}

	public abstract T doCalculate(BigInteger index);

}
