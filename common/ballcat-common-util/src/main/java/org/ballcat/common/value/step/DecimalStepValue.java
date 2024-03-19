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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import org.ballcat.common.value.StepValue;

/**
 * @author lingting 2024-02-27 14:15
 */
public class DecimalStepValue extends AbstractConcurrentStepValue<BigDecimal> {

	/**
	 * 与初始值
	 */
	protected final BigDecimal start;

	/**
	 * 每次步进值
	 */
	protected final BigDecimal step;

	/**
	 * 最大步进次数, 为null表示无限步进次数
	 */
	protected final BigInteger maxIndex;

	/**
	 * 最大值(可以等于), 为null表示无最大值限制
	 */
	protected final BigDecimal maxValue;

	public DecimalStepValue(BigDecimal step, BigInteger maxIndex, BigDecimal maxValue) {
		this(BigDecimal.ZERO, step, maxIndex, maxValue);
	}

	public DecimalStepValue(BigDecimal start, BigDecimal step, BigInteger maxIndex, BigDecimal maxValue) {
		if (start == null || step == null) {
			throw new IllegalArgumentException(
					String.format("Invalid start value[%s] or step value[%s].", start, step));
		}
		this.start = start;
		this.step = step;
		this.maxIndex = maxIndex;
		this.maxValue = maxValue;
	}

	public DecimalStepValue start(BigDecimal start) {
		return new DecimalStepValue(start, this.step, this.maxIndex, this.maxValue);
	}

	@Override
	public BigDecimal firt() {
		return this.start;
	}

	@Override
	public StepValue<BigDecimal> copy() {
		return start(this.start);
	}

	@Override
	public boolean doHasNext() {
		// 当前索引位置大于等于最大索引位置则不可以继续步进
		if (this.maxIndex != null && this.index.compareTo(this.maxIndex) > -1) {
			return false;
		}
		// 下一个值大于最大值则不可以继续步进
		if (this.maxValue != null) {
			BigDecimal next = calculateNext();
			return next.compareTo(this.maxValue) < 1;
		}
		return true;
	}

	@Override
	public BigDecimal doCalculate(BigInteger index) {
		BigDecimal decimal = new BigDecimal(index);
		// 步进值 * 索引位置 = 增长值
		BigDecimal growth = this.step.multiply(decimal, MathContext.UNLIMITED);
		// 与初始值相加
		return this.start.add(growth);
	}

}
