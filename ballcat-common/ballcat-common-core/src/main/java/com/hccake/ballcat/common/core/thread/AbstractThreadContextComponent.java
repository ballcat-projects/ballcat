package com.hccake.ballcat.common.core.thread;

import com.hccake.ballcat.common.core.compose.ContextComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingting 2023-04-22 10:40
 */
@Slf4j
public abstract class AbstractThreadContextComponent extends Thread implements ContextComponent {

	protected void init() {

	}

	public boolean isRun() {
		return !isInterrupted() && isAlive();
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

	public String getSimpleName() {
		return getClass().getSimpleName();
	}

	@Override
	public abstract void run();

}
