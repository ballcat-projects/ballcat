package com.hccake.ballcat.common.redis.thread;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.core.thread.AbstractQueueThread;
import com.hccake.ballcat.common.redis.RedisHelper;
import com.hccake.ballcat.common.util.JsonUtils;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * @see java.util.concurrent.LinkedBlockingDeque
 * @author lingting 2021/3/2 21:09
 */
@Slf4j
public abstract class AbstractRedisThread<E> extends AbstractQueueThread<E> {

	@Autowired
	protected RedisHelper redisHelper;

	/**
	 * 是否正在运行
	 */
	protected boolean run = true;

	/**
	 * 锁
	 */
	protected final ReentrantLock lock = new ReentrantLock();

	/**
	 * 激活与休眠线程
	 */
	protected final Condition condition = lock.newCondition();

	/**
	 * 获取数据存储的key
	 * @return java.lang.String
	 * @author lingting 2021-03-02 21:11
	 */
	public abstract String getKey();

	/**
	 * 对象 转换成 string. 把String 存入redis
	 * @param e 对象
	 * @return java.lang.String
	 * @author lingting 2021-03-02 21:38
	 */
	protected String convertToString(@NotNull E e) {
		return JsonUtils.toJson(e);
	}

	/**
	 * 获取目标对象的type , 即 E 的实际类型. 如果获取失败, 请重写此方法
	 * @return java.lang.reflect.Type
	 * @author lingting 2021-03-02 21:41
	 */
	protected Type getObjType() {
		return ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * string 转换成 对象
	 * @param str string
	 * @return java.lang.String
	 * @author lingting 2021-03-02 21:38
	 */
	@Nullable
	protected E convertToObj(String str) {
		if (StrUtil.isBlank(str)) {
			return null;
		}
		return JsonUtils.toObj(str, getObjType());
	}

	@Override
	public void put(E e) {
		// 不插入空值
		if (e != null) {
			try {
				lock.lockInterruptibly();
				try {
					// 线程被中断后无法执行Redis命令
					RedisHelper.listRightPush(getKey(), convertToString(e));
					// 激活线程
					condition.signal();
				}
				finally {
					lock.unlock();
				}
			}
			catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			catch (Exception ex) {
				log.error("{} put error, param: {}", this.getClass().toString(), e, ex);
			}
		}
	}

	/**
	 * 从redis中获取数据
	 *
	 * 忽略sonar 警告. 子类有可能需要在get时做其他操作.
	 * @return java.lang.String
	 * @author lingting 2021-03-02 22:04
	 */
	@SuppressWarnings("java:S2177")
	protected String get() {
		return RedisHelper.listLeftPop(getKey());
	}

	@Override
	@Nullable
	public E poll(long time) throws InterruptedException {
		if (!isRun()) {
			// 停止运行时返回null, 让数据待在redis里面
			return null;
		}
		// 上锁
		lock.lockInterruptibly();
		try {
			// 设置等待时长
			long nanos = TimeUnit.MILLISECONDS.toNanos(time);
			String pop;
			do {
				// 获取数据
				pop = get();
				if (StringUtils.hasText(pop)) {
					break;
				}

				// 休眠. 返回剩余的休眠时间
				nanos = condition.awaitNanos(nanos);
			}
			while (isRun() && nanos > 0);

			return convertToObj(pop);
		}
		finally {
			lock.unlock();
		}

	}

	@Override
	public void shutdown() {
		// 修改运行标志
		run = false;
		lock.lock();
		try {
			condition.signalAll();
		}
		finally {
			lock.unlock();
		}
	}

	@Override
	public void shutdownHandler(List<E> list) {
		log.warn("{} 线程被关闭! id: {}", getClass().getSimpleName(), getId());
		for (E e : list) {
			// 所有数据插入redis
			put(e);
			log.error("{}", e);
		}
	}

	@Override
	public boolean isRun() {
		// 运行中 且 未被中断
		return run && !isInterrupted();
	}

}
