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

import lombok.RequiredArgsConstructor;
import org.ballcat.common.value.StepValue;

/**
 * @author lingting 2024-01-23 15:22
 */
@RequiredArgsConstructor
public class StepCycleValue<T> extends AbstractConcurrentCycleValue<T> {

	private final StepValue<T> step;

	@Override
	public void doReset() {
		this.step.reset();
	}

	@Override
	public T doNext() {
		if (!this.step.hasNext()) {
			this.step.reset();
		}
		return this.step.next();
	}

}
