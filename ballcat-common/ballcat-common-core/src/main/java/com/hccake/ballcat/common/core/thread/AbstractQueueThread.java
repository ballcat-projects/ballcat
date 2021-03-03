package com.hccake.ballcat.common.core.thread;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;

/**
 * 顶级队列线程类
 *
 * @author lingting 2021/3/2 15:07
 */
public abstract class AbstractQueueThread<E> extends Thread implements InitializingBean {

	/**
	 * 默认缓存数据数量
	 */
	private final static int DEFAULT_BATCH_SIZE = 500;

	/**
	 * 默认等待时长 30秒；单位 毫秒
	 */
	private final static long DEFAULT_BATCH_TIMEOUT_MS = 30 * 1000L;

	/**
	 * 默认获取数据时的超时时间
	 */
	private final static long POLL_TIMEOUT_MS = 5 * 1000;

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
	public abstract void process(List<E> list) throws Exception;

	@Override
	public void run() {
		init();
		List<E> list;
		while (isRun()) {
			list = new ArrayList<>(getBatchSize());

			try {
				preProcess();
				long timestamp = 0;
				int count = 0;

				while (count < getBatchSize()) {
					E e = null;
					try {
						e = poll(getPollTimeoutMs());
					}
					catch (InterruptedException interruptedException) {
						interruptedException.printStackTrace();
					}

					if (e != null) {
						// 第一次插入数据
						if (count++ == 0) {
							// 记录时间
							timestamp = System.currentTimeMillis();
						}
						receiveProcess(list, e);
					}

					// 已有数据 已超过设定的等待时间
					if (list.size() > 0 && System.currentTimeMillis() - timestamp >= getBatchTimeout()) {
						break;
					}
				}
				process(list);
			}
			catch (Throwable e) {
				error(e, list);
			}
		}
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
		start();
	}

}
