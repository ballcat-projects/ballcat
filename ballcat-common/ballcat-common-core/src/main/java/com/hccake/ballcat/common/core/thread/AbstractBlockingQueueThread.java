package com.hccake.ballcat.common.core.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象的线程类，主要用于汇聚详情数据 做一些基础的处理后 进行批量插入
 *
 * @author Hccake
 */
@Slf4j
public abstract class AbstractBlockingQueueThread<T> extends AbstractQueueThread<T> {

	private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

	public void putObject(T t) {
		try {
			if (t != null) {
				queue.put(t);
			}
		}
		catch (Exception e) {
			log.error("{} putObject error, param: {}", this.getClass().toString(), t, e);
		}
	}

	@Override
	public void put(@NotNull T t) throws InterruptedException {
		queue.put(t);
	}

	@Override
	public T poll(long time) throws InterruptedException {
		return queue.poll(time, TimeUnit.MILLISECONDS);
	}

}
