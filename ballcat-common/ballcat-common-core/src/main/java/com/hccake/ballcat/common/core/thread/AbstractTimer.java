package com.hccake.ballcat.common.core.thread;

import com.hccake.ballcat.common.core.compose.ContextComponent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2022/6/27 20:26
 */
@Slf4j
public abstract class AbstractTimer extends Thread implements ContextComponent {

	/**
	 * 获取超时时间, 单位: 毫秒
	 */
	public long getTimeout() {
		return TimeUnit.SECONDS.toMillis(30);
	}

	public boolean isRun() {
		return !isInterrupted();
	}

	/**
	 * 运行前执行初始化
	 */
	protected void init() {
	}

	/**
	 * 执行任务
	 */
	protected abstract void process();

	/**
	 * 线程被中断触发.
	 */
	protected void shutdown() {
	}

	protected void error(Exception e) {
		log.error("{} 类 线程: {} 出现异常!", getClass().getSimpleName(), getId(), e);
	}

	@Override
	public void run() {
		init();
		while (isRun()) {
			try {
				process();

				// 已经停止运行, 结束
				if (!isRun()) {
					shutdown();
					return;
				}

				Thread.sleep(getTimeout());
			}
			catch (InterruptedException e) {
				interrupt();
				shutdown();
			}
			catch (Exception e) {
				error(e);
			}
		}
	}

	@Override
	public void onApplicationStart() {
		setName(getClass().getSimpleName());
		if (!isAlive()) {
			start();
		}
	}

	@Override
	public void onApplicationStop() {
		log.warn("{} 线程: {}; 开始关闭!", getClass().getSimpleName(), getId());
		interrupt();
	}

}
