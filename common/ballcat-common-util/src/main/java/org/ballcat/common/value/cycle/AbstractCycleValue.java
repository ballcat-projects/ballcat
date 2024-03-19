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

package org.ballcat.common.value.cycle;

import java.math.BigInteger;

import org.ballcat.common.value.CycleValue;

/**
 * @author lingting 2024-02-27 19:19
 */
public abstract class AbstractCycleValue<T> implements CycleValue<T> {

	protected BigInteger count = BigInteger.ZERO;

	@Override
	public BigInteger count() {
		return this.count;
	}

	@Override
	public T next() {
		this.count = this.count.add(BigInteger.ONE);
		return doNext();
	}

	public abstract T doNext();

}
