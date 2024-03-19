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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.ballcat.common.value.StepValue;

/**
 * @author lingting 2024-02-27 14:11
 */
public abstract class AbstractStepValue<T> implements StepValue<T> {

	public static final BigInteger DEFAULT_INDEX = BigInteger.valueOf(0);

	protected BigInteger index = DEFAULT_INDEX;

	protected List<T> values;

	@Override
	public BigInteger index() {
		return this.index;
	}

	@Override
	public void reset() {
		this.index = DEFAULT_INDEX;
	}

	@Override
	public List<T> values() {
		if (this.values != null) {
			return this.values;
		}
		List<T> list = new ArrayList<>();
		StepValue<T> copy = copy();
		while (copy.hasNext()) {
			list.add(copy.next());
		}
		this.values = list;
		return list;
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		BigInteger increasing = increasing();
		if (increasing.compareTo(BigInteger.ZERO) == 0) {
			return firt();
		}
		return calculate(increasing);
	}

	public BigInteger increasing() {
		BigInteger current = this.index.add(BigInteger.ONE);
		this.index = current;
		return current;
	}

	public T calculateNext() {
		return calculate(this.index.add(BigInteger.ONE));
	}

	public abstract T calculate(BigInteger calculateIndex);

}
