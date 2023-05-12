package com.hccake.ballcat.common.queue;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2023/1/29 10:52
 */
public class WaitQueue<V> {

	private final LinkedBlockingQueue<V> queue;

	public WaitQueue() {
		this(new LinkedBlockingQueue<>());
	}

	public WaitQueue(LinkedBlockingQueue<V> queue) {
		this.queue = queue;
	}

	public V get() {
		return queue.poll();
	}

	public V poll() throws InterruptedException {
		return poll(10, TimeUnit.HOURS);
	}

	public V poll(long timeout, TimeUnit unit) throws InterruptedException {
		V v;
		do {
			v = queue.poll(timeout, unit);
		}
		while (v == null);
		return v;
	}

	public void clear() {
		queue.clear();
	}

	public void add(V seat) {
		queue.add(seat);
	}

	public void addAll(Collection<V> accounts) {
		for (V account : accounts) {
			add(account);
		}
	}

}
