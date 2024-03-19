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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.ballcat.common.value.step.DecimalStepValue;
import org.ballcat.common.value.step.IteratorStepValue;
import org.ballcat.common.value.step.LongStepValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2024-03-19 10:06
 */
class StepValueTest {

	void assertNumber(StepValue<? extends Number> step) {
		assertTrue(step.hasNext());
		assertEquals(1, step.next().longValue());
		assertEquals(2, step.next().longValue());
		assertEquals(3, step.next().longValue());
		assertFalse(step.hasNext());
		assertThrowsExactly(NoSuchElementException.class, step::next);
		List<? extends Number> values = step.values();
		assertEquals(3, values.size());
		assertEquals(1, values.get(0).longValue());
	}

	@Test
	void testLong() {
		LongStepValue step = new LongStepValue(1, 3L, null);
		assertNumber(step);
		StepValue<Long> copy = step.copy();
		assertNumber(copy);

		StepValue<Long> max = new LongStepValue(5, null, 15L).start(5L);
		assertEquals(10, max.next());
		assertEquals(15, max.next());
		assertFalse(max.hasNext());
	}

	@Test
	void testDecimal() {
		DecimalStepValue step = new DecimalStepValue(BigDecimal.ONE, BigInteger.valueOf(3), null);
		assertNumber(step);
		StepValue<BigDecimal> copy = step.copy();
		assertNumber(copy);

		DecimalStepValue max = new DecimalStepValue(BigDecimal.valueOf(5), null, BigDecimal.valueOf(15))
			.start(BigDecimal.valueOf(5));
		assertEquals(10, max.next().longValue());
		assertEquals(15, max.next().longValue());
		assertFalse(max.hasNext());
	}

	@Test
	void testIterator() {
		List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
		IteratorStepValue<Integer> step = new IteratorStepValue<>(list.iterator());
		assertNumber(step);
		StepValue<Integer> copy = step.copy();
		assertNumber(copy);
		IteratorStepValue<Integer> remove = new IteratorStepValue<>(list.iterator());
		List<Integer> values = remove.values();
		assertEquals(3, values.size());
		assertThrowsExactly(IllegalStateException.class, remove::remove);
		assertEquals(1, remove.next());
		assertDoesNotThrow(remove::remove);
		assertEquals(BigInteger.ZERO, remove.index());
		assertEquals(2, remove.next());
		assertEquals(3, remove.next());
		assertDoesNotThrow(remove::remove);
		assertFalse(remove.hasNext());
		assertThrowsExactly(NoSuchElementException.class, remove::next);
		remove.reset();
		assertEquals(2, remove.next());
		assertFalse(remove.hasNext());
		assertDoesNotThrow(remove::remove);
		remove.reset();
		assertFalse(remove.hasNext());
	}

}
