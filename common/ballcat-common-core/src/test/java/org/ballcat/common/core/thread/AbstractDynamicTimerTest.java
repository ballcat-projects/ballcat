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

package org.ballcat.common.core.thread;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.util.LocalDateTimeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2023-04-22 11:18
 */
@Slf4j
@SuppressWarnings("java:S2925")
class AbstractDynamicTimerTest {

	DynamicTimer timer;

	@BeforeEach
	void before() {
		this.timer = new DynamicTimer();
		this.timer.start();
	}

	@AfterEach
	void after() {
		this.timer.interrupt();
	}

	@Test
	void test() throws InterruptedException {
		LocalDateTime now = LocalDateTime.now();
		Action a1 = new Action("1", now.plusSeconds(1));
		Action a2 = new Action("2", now.plusSeconds(2));
		Action a4 = new Action("4", now.plusSeconds(4));

		this.timer.put(a1);
		this.timer.put(a2);
		this.timer.put(a4);

		TimeUnit.MILLISECONDS.sleep(1050);
		Assertions.assertEquals(1, this.timer.getProcessedCount());

		TimeUnit.SECONDS.sleep(1);
		Assertions.assertEquals(2, this.timer.getProcessedCount());

		this.timer.put(a2);
		TimeUnit.MILLISECONDS.sleep(2050);
		Assertions.assertEquals(4, this.timer.getProcessedCount());
	}

	@Data
	@AllArgsConstructor
	public static class Action {

		private String id;

		private LocalDateTime execTime;

	}

	/**
	 * 按照执行时间依次执行.
	 */
	private static class DynamicTimer extends AbstractDynamicTimer<Action> {

		private final AtomicInteger processedCount = new AtomicInteger(0);

		@Override
		public Comparator<Action> comparator() {
			return (o1, o2) -> {
				System.out.printf("【comparator】运算优先级. o1: %s, o2: %s%n", o1.id, o2.id);
				Duration duration = Duration.between(o1.execTime, o2.execTime);
				if (duration.isZero()) {
					return 0;
				}
				return duration.isNegative() ? 1 : -1;
			};
		}

		@Override
		protected long sleepTime(Action action) {
			LocalDateTime now = LocalDateTime.now();
			Duration duration = Duration.between(now, action.execTime);
			long millis = duration.toMillis();
			System.out.printf("【sleepTime】当前时间: %s; 计算休眠时间. %s. 休眠: %d毫秒%n", LocalDateTimeUtils.format(now), action.id,
					millis);
			return millis;
		}

		@Override
		protected void process(Action action) {
			String format = LocalDateTimeUtils.format(LocalDateTime.now());
			System.out.printf("【process】当前时间: %s; 执行: %s; 预计执行时间: %s%n", format, action.id,
					LocalDateTimeUtils.format(action.execTime));
			this.processedCount.incrementAndGet();
		}

		public int getProcessedCount() {
			return this.processedCount.get();
		}

	}

}
