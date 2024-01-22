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

package org.ballcat.common.system;

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
		if (this.startTimeNanos != null) {
			return;
		}
		this.durationNanos = null;
		this.startTimeNanos = System.nanoTime();
	}

	/**
	 * 是否正在运行
	 */
	public boolean isRunning() {
		return this.startTimeNanos != null;
	}

	public void stop() {
		if (this.startTimeNanos != null) {
			this.durationNanos = System.nanoTime() - this.startTimeNanos;
		}
		this.startTimeNanos = null;
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
		if (this.durationNanos == null) {
			return this.startTimeNanos == null ? 0 : System.nanoTime() - this.startTimeNanos;
		}
		return this.durationNanos;
	}

	public long timeMillis() {
		return TimeUnit.NANOSECONDS.toMillis(timeNanos());
	}

}
