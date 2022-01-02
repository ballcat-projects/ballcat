package com.hccake.ballcat.common.core.thread;

import com.hccake.ballcat.common.util.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

/**
 * 顶级队列线程类
 *
 * @author lingting 2021/3/2 15:07
 */
@Slf4j
public abstract class AbstractQueueThread<E> extends Thread
		implements InitializingBean, ApplicationListener<ContextClosedEvent> {

	/**
	 * 默认缓存数据数量
	 */
	private static final int DEFAULT_BATCH_SIZE = 500;

	/**
	 * 默认等待时长 30秒；单位 毫秒
	 */
	private static final long DEFAULT_BATCH_TIMEOUT_MS = 30 * 1000L;

	/**
	 * 默认获取数据时的超时时间
	 */
	private static final long POLL_TIMEOUT_MS = 5 * 1000L;

	/**
	 * 用于子类自定义缓存数据数量
	 * @return long
	 * @author lingting 2021-03-02 15:12
	 */
	public int getBatchSize() {
		return DEFAULT_BATCH_SIZE;
	}

	/**
	 * 用于子类自定义等待时长
	 * @return 返回时长，单位毫秒
	 * @author lingting 2020-08-05 11:23:33
	 */
	public long getBatchTimeout() {
		return DEFAULT_BATCH_TIMEOUT_MS;
	}

	/**
	 * 用于子类自定义 获取数据的超时时间
	 * @return 返回时长，单位毫秒
	 * @author lingting 2021-03-02 20:52
	 */
	public static long getPollTimeoutMs() {
		return POLL_TIMEOUT_MS;
	}

	/**
	 * 往队列插入数据
	 * @param e 数据
	 * @author lingting 2021-03-02 15:09
	 */
	public abstract void put(@NotNull E e);

	/**
	 * 运行前执行初始化
	 * @author lingting 2021-03-02 15:14
	 */
	public void init() {
	}

	/**
	 * 是否可以继续运行
	 * @return boolean true 表示可以继续运行
	 * @author lingting 2021-03-02 15:17
	 */
	public boolean isRun() {
		// 未被中断表示可以继续运行
		return !isInterrupted();
	}

	/**
	 * 数据处理前执行
	 * @author lingting 2021-03-02 15:15
	 */
	public void preProcess() {
	}

	/**
	 * 从队列中取值
	 * @param time 等待时长, 单位 毫秒
	 * @return E
	 * @throws InterruptedException 线程中断
	 * @author lingting 2021-03-02 15:20
	 */
	@Nullable
	public abstract E poll(long time) throws InterruptedException;

	/**
	 * 处理接收的数据
	 * @param list 当前所有数据
	 * @param e 接收的数据
	 * @author lingting 2021-03-02 20:49
	 */
	public void receiveProcess(List<E> list, E e) {
		list.add(e);
	}

	/**
	 * 处理所有已接收的数据
	 * @param list 所有已接收的数据
	 * @exception Exception 异常
	 * @author lingting 2021-03-02 20:53
	 */
	@SuppressWarnings("java:S112")
	public abstract void process(List<E> list) throws Exception;

	@Override
	@SuppressWarnings("java:S1181")
	public void run() {
		init();
		List<E> list;
		while (isRun()) {
			list = new ArrayList<>(getBatchSize());

			try {
				preProcess();
				fillList(list);

				if (!isRun()) {
					shutdownHandler(list);
				}
				else {
					process(list);
				}
			}
			catch (Exception e) {
				error(e, list);
			}
			// Throwable 异常直接结束. 这里捕获用来保留信息. 方便排查问题
			catch (Throwable t) {
				log.error("线程队列运行异常!", t);
				throw t;
			}
		}
	}

	protected void fillList(List<E> list) {
		long timestamp = 0;
		int count = 0;

		while (count < getBatchSize()) {
			E e = get();

			if (e != null) {
				// 第一次插入数据
				if (count++ == 0) {
					// 记录时间
					timestamp = System.currentTimeMillis();
				}
				receiveProcess(list, e);
			}

			// 无法继续运行 或 已有数据且超过设定的等待时间
			final boolean isBreak = !isRun()
					|| (!CollectionUtils.isEmpty(list) && System.currentTimeMillis() - timestamp >= getBatchTimeout());
			if (isBreak) {
				break;
			}
		}
	}

	private E get() {
		E e = null;
		try {
			e = poll(getPollTimeoutMs());
		}
		catch (InterruptedException ex) {
			interrupt();
			log.error("{} 类的poll线程被中断!id: {}", getClass().getSimpleName(), getId());
		}
		return e;
	}

	/**
	 * 发生异常时处理异常
	 * @param e 异常
	 * @param list 当时的数据
	 * @author lingting 2021-03-02 20:46
	 */
	public abstract void error(Throwable e, List<E> list);

	@Override
	public void afterPropertiesSet() throws Exception {
		// 默认配置线程名. 用来方便查询
		setName(this.getClass().getSimpleName());
		if (!this.isAlive()) {
			this.start();
		}
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		log.warn("{} 类的线程开始关闭! id: {} ", getClass().getSimpleName(), getId());
		// 执行关闭方法
		shutdown();
	}

	/**
	 * 线程关闭时执行
	 * @author lingting 2021-03-08 22:25
	 */
	public void shutdown() {
		// 通过中断线程唤醒当前线程. 让线程进入 shutdownHandler 方法处理数据
		this.interrupt();
	}

	/**
	 * 线程被中断后的处理. 如果有缓存手段可以让数据进入缓存.
	 * @param list 当前数据
	 * @author lingting 2021-03-08 22:40
	 */
	public void shutdownHandler(List<E> list) {
		try {
			log.error("{} 类 线程: {} 被关闭. 数据:{}", this.getClass().getSimpleName(), getId(), JsonUtils.toJson(list));
		}
		catch (Throwable e) {
			log.error("{} 类 线程: {} 被关闭. 数据:{}", this.getClass().getSimpleName(), getId(), list);

		}
	}

}
