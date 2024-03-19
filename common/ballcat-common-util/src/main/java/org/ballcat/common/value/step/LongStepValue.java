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

import org.ballcat.common.value.StepValue;

/**
 * @author lingting 2024-02-27 15:44
 */
public class LongStepValue extends AbstractConcurrentStepValue<Long> {

	/**
	 * 与初始值
	 */
	protected final Long start;

	/**
	 * 每次步进值
	 */
	protected final Long step;

	/**
	 * 最大步进次数, 为null表示无限步进次数
	 */
	protected final Long maxIndex;

	/**
	 * 最大值(可以等于), 为null表示无最大值限制
	 */
	protected final Long maxValue;

	public LongStepValue(long step, long maxIndex, long maxValue) {
		this(step, Long.valueOf(maxIndex), Long.valueOf(maxValue));
	}

	public LongStepValue(long step, Long maxIndex, Long maxValue) {
		this(0, step, maxIndex, maxValue);
	}

	public LongStepValue(long start, Long step, Long maxIndex, Long maxValue) {
		if (step == null) {
			throw new IllegalArgumentException(String.format("Invalid step value[%d].", step));
		}
		this.start = start;
		this.step = step;
		this.maxIndex = maxIndex;
		this.maxValue = maxValue;
	}

	public LongStepValue start(Long start) {
		return new LongStepValue(start, this.step, this.maxIndex, this.maxValue);
	}

	@Override
	public Long firt() {
		return this.start;
	}

	@Override
	public StepValue<Long> copy() {
		return start(this.start);
	}

	@Override
	public boolean doHasNext() {
		// 当前索引位置大于等于最大索引位置则不可以继续步进
		if (this.maxIndex != null && this.index.longValue() >= this.maxIndex) {
			return false;
		}
		// 下一个值大于最大值则不可以继续步进
		if (this.maxValue != null) {
			Long next = calculateNext();
			return next.compareTo(this.maxValue) < 1;
		}
		return true;
	}

	@Override
	public Long doCalculate(BigInteger index) {
		Long decimal = index.longValue();
		// 步进值 * 索引位置 = 增长值
		long growth = this.step * decimal;
		// 与初始值相加
		return this.start + growth;
	}

}
