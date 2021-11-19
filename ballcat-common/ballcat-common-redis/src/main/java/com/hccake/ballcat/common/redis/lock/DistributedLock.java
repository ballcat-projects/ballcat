package com.hccake.ballcat.common.redis.lock;

import cn.hutool.core.lang.Assert;
import com.hccake.ballcat.common.redis.core.CacheLock;
import com.hccake.ballcat.common.redis.lock.function.ThrowingSupplier;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author huyuanzhi
 * @version 1.0
 * @date 2021/11/16 分布式锁操作类
 */
public final class DistributedLock<T> implements Action<T>, StateHandler<T> {

	Object result;

	String key;

	ThrowingSupplier<? extends T> executeAction;

	Function<? super T, ? extends T> successAction;

	Supplier<? extends T> lockFailAction;

	Consumer<? super Throwable> exceptionAction;

	private DistributedLock() {
		this.exceptionAction = this::throwException;
	}

	public static <T> Action<T> builder() {
		return new DistributedLock<>();
	}

	public StateHandler<T> action(String lockKey, ThrowingSupplier<? extends T> action) {
		Assert.isTrue(this.executeAction == null, "execute action has been already set");
		Assert.notNull(action, "execute action cant be null");
		Assert.notBlank(lockKey, "lock key cant be blank");
		this.executeAction = action;
		this.key = lockKey;
		return this;
	}

	public StateHandler<T> success(Function<? super T, ? extends T> action) {
		Assert.isTrue(this.successAction == null, "success action has been already set");
		Assert.notNull(action, "success action cant be null");
		this.successAction = action;
		return this;
	}

	public StateHandler<T> fail(Supplier<? extends T> action) {
		Assert.isTrue(this.lockFailAction == null, "lock fail action has been already set");
		Assert.notNull(action, "lock fail action cant be null");
		this.lockFailAction = action;
		return this;
	}

	public StateHandler<T> exception(Consumer<? super Throwable> action) {
		Assert.notNull(action, "exception action cant be null");
		this.exceptionAction = action;
		return this;
	}

	@SuppressWarnings("unchecked")
	public T lock() {
		String requestId = UUID.randomUUID().toString();
		if (Boolean.TRUE.equals(CacheLock.lock(this.key, requestId))) {
			T ret = null;
			boolean exResolved = false;
			try {
				ret = executeAction.get();
				this.setValue(ret);
			}
			catch (Throwable e) {
				handleException(e);
				exResolved = true;
			}
			finally {
				CacheLock.releaseLock(this.key, requestId);
			}
			if (!exResolved && this.successAction != null) {
				this.setValue(this.successAction.apply(ret));
			}
		}
		else {
			if (lockFailAction != null) {
				this.setValue(lockFailAction.get());
			}
		}
		return (T) this.result;
	}

	private void handleException(Throwable e) {
		this.exceptionAction.accept(e);
	}

	private void setValue(Object result) {
		this.result = result;
	}

	@SuppressWarnings("unchecked")
	private <E extends Throwable> void throwException(Throwable t) throws E {
		throw (E) t;
	}

}
