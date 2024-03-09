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

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lingting 2022/6/27 20:26
 */
public abstract class AbstractTimer extends Thread {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 获取超时时间, 单位: 毫秒
	 */
	public long getTimeout() {
		return TimeUnit.SECONDS.toMillis(30);
	}

	/**
	 * 执行任务
	 */
	protected abstract void process();

	protected void init() {
	}

	public boolean isRun() {
		return !isInterrupted() && isAlive();
	}

	/**
	 * 线程被中断触发.
	 */
	protected void shutdown() {
		this.log.warn("{} 类 线程: {} 被中断!", getClass().getSimpleName(), getId());
	}

	protected void error(Exception e) {
		this.log.error("{} 类 线程: {} 出现异常!", getClass().getSimpleName(), getId(), e);
	}

	@Override
	public void run() {
		init();
		try {
			while (isRun()) {
				doRun();
			}
		}
		catch (InterruptedException e) {
			interrupt();
			shutdown();
		}
	}

	protected void doRun() throws InterruptedException {
		try {
			process();
			// 已经停止运行, 结束
			if (!isRun()) {
				shutdown();
			}
		}
		catch (Exception e) {
			error(e);
		}
		finally {
			Thread.sleep(getTimeout());
		}
	}

}
