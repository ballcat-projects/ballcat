package com.hccake.ballcat.common.core.thread;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author lingting 2022/6/27 20:26
 */
@Slf4j
public abstract class AbstractTimer extends Thread implements InitializingBean {

	/**
	 * 获取超时时间, 单位: 毫秒
	 */
	public long getTimeout() {
		return TimeUnit.SECONDS.toMillis(30);
	}

	public boolean isRun() {
		return !isInterrupted();
	}

	public abstract void process();

	/**
	 * 休眠中线程被中止触发.
	 */
	public void shutdown() {
		log.error("{} 类 线程: {} 被关闭.", getClass().getSimpleName(), getId());
	}

	public void error(Exception e) {
		log.error("{} 类 线程: {} 出现异常!", getClass().getSimpleName(), getId(), e);
	}

	@Override
	public void run() {
		while (isRun()) {
			try {
				process();
				try {
					Thread.sleep(getTimeout());
				}
				catch (InterruptedException e) {
					// sonar
					Thread.currentThread().interrupt();
					shutdown();
				}
			}
			catch (Exception e) {
				error(e);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setName(getClass().getSimpleName());
		if (!isAlive()) {
			start();
		}
	}

}
