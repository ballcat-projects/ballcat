package com.ballcat.core.thread;

import com.hccake.ballcat.common.core.thread.AbstractDynamicTimer;
import com.hccake.ballcat.common.util.LocalDateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * @author lingting 2023-04-22 11:18
 */
@Slf4j
@SuppressWarnings("java:S2925")
class AbstractDynamicTimerTest {

	DynamicTimer timer;

	@BeforeEach
	void before() {
		timer = new DynamicTimer();
		timer.onApplicationStart();
	}

	@AfterEach
	void after() {
		timer.onApplicationStop();
	}

	@Test
	void test() throws InterruptedException {
		LocalDateTime now = LocalDateTime.now();
		Action a10 = new Action("10", now.plusSeconds(10));
		Action a5 = new Action("5", now.plusSeconds(5));
		Action a30 = new Action("30", now.plusSeconds(30));
		Action a2 = new Action("2", now.plusSeconds(2));
		Action a20 = new Action("20", now.plusSeconds(20));
		Action a15 = new Action("15", now.plusSeconds(15));

		timer.put(a10);
		timer.put(a5);
		timer.put(a30);

		sleep(3);
		timer.put(a2);

		sleep(12);
		timer.put(a15);
		sleep(10);
		timer.put(a20);

		sleep(20);
		Assertions.assertEquals(6, timer.count);
	}

	void sleep(long seconds) throws InterruptedException {
		Thread.sleep(seconds * 1000);
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
	public static class DynamicTimer extends AbstractDynamicTimer<Action> {

		private long count = 0;

		@Override
		public Comparator<Action> comparator() {
			return (o1, o2) -> {
				System.out.printf("运算优先级. o1: %s, o2: %s%n", o1.id, o2.id);
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
			System.out.printf("当前时间: %s; 计算休眠时间. %s. 休眠: %d毫秒%n", LocalDateTimeUtils.format(now), action.id, millis);
			return millis;
		}

		@Override
		protected void process(Action action) {
			String format = LocalDateTimeUtils.format(LocalDateTime.now());
			System.out.printf("当前时间: %s; 执行: %s; 预计执行时间: %s%n", format, action.id,
					LocalDateTimeUtils.format(action.execTime));
			count += 1;
		}

	}

}
