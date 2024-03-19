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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.ballcat.common.value.cycle.IteratorCycleValue;
import org.ballcat.common.value.cycle.StepCycleValue;
import org.ballcat.common.value.step.LongStepValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

/**
 * @author lingting 2024-03-19 10:06
 */
class CycleValueTest {

	void assertNumber(CycleValue<? extends Number> cycle) {
		assertEquals(1, cycle.next().longValue());
		assertEquals(2, cycle.next().longValue());
		assertEquals(3, cycle.next().longValue());
		assertEquals(1, cycle.next().longValue());
		assertEquals(2, cycle.next().longValue());
		assertEquals(3, cycle.next().longValue());
		assertEquals(1, cycle.next().longValue());
		cycle.reset();
		assertEquals(1, cycle.next().longValue());
		assertEquals(8, cycle.count().longValue());
	}

	@Test
	void testStep() {
		StepCycleValue<Long> cycle = new StepCycleValue<>(new LongStepValue(1, 3, 99));
		assertNumber(cycle);
	}

	@Test
	void testIterator() {
		List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
		IteratorCycleValue<Integer> cycle = new IteratorCycleValue<>(list.iterator());
		assertNumber(cycle);
		cycle.reset();
		assertEquals(1, cycle.next());
		assertDoesNotThrow(cycle::remove);
		assertEquals(2, cycle.next());
		assertEquals(3, cycle.next());
		assertDoesNotThrow(cycle::remove);
		assertEquals(2, cycle.next());
		cycle.reset();
		assertThrowsExactly(IllegalStateException.class, cycle::remove);
		assertEquals(2, cycle.next());
		assertDoesNotThrow(cycle::remove);
		assertThrowsExactly(NoSuchElementException.class, cycle::next);
	}

}
