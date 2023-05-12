package com.hccake.ballcat.common.system;

import java.util.concurrent.TimeUnit;

/**
 * 参考 org.springframework.util.StopWatch
 * <p>
 * 线程不安全
 * </p>
 *
 * @author lingting 2023-02-15 16:47
 */
public class StopWatch {

	private Long startTimeNanos;

	/**
	 * 耗时, 单位: 纳秒
	 */
	private Long durationNanos;

	/**
	 * 开始计时, 如果已开始, 则从延续之前的计时
	 */
	public void start() {
		if (startTimeNanos != null) {
			return;
		}
		durationNanos = null;
		startTimeNanos = System.nanoTime();
	}

	/**
	 * 是否正在运行
	 */
	public boolean isRunning() {
		return startTimeNanos != null;
	}

	public void stop() {
		if (startTimeNanos != null) {
			durationNanos = System.nanoTime() - startTimeNanos;
		}
		startTimeNanos = null;
	}

	public void restart() {
		stop();
		start();
	}

	/**
	 * 获取执行时长
	 * <p>
	 * 如果已开始, 则是开始时间到当前时长
	 * </p>
	 * <p>
	 * 如果已结束, 则是上一次统计时长
	 * </p>
	 */
	public long timeNanos() {
		if (durationNanos == null) {
			return startTimeNanos == null ? 0 : System.nanoTime() - startTimeNanos;
		}
		return durationNanos;
	}

	public long timeMillis() {
		return TimeUnit.NANOSECONDS.toMillis(timeNanos());
	}

}
