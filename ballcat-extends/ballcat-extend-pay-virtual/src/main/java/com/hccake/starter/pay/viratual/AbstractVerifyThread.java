package com.hccake.starter.pay.viratual;

import com.hccake.ballcat.common.redis.thread.AbstractRedisThread;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import live.lingting.virtual.currency.Transaction;

/**
 * 用于校验交易的线程
 *
 * @author lingting 2021/1/5 11:08
 */
@Slf4j
public abstract class AbstractVerifyThread<T extends VerifyObj, R> extends AbstractRedisThread<T> {

	@Override
	public int getBatchSize() {
		// 校验对象不需要堆积太多, 10条直接处理
		return 10;
	}

	/**
	 * 根据校验对象信息获取交易数据
	 * @param obj 校验对象信息
	 * @return live.lingting.virtual.currency.Transaction
	 * @author lingting 2021-01-05 11:22
	 */
	public abstract Optional<Transaction> getTransaction(T obj);

	/**
	 * 处理交易
	 * @param obj 校验对象信息
	 * @param optional 交易数据
	 * @author lingting 2021-01-05 11:13
	 */
	public abstract void handler(T obj, @NotNull Optional<Transaction> optional);

	/**
	 * 失败处理
	 * @param obj 校验对象
	 * @param optional 交易数据
	 * @param r 处理结果
	 * @author lingting 2021-01-05 11:18
	 */
	public abstract void failed(T obj, @NotNull Optional<Transaction> optional, R r);

	/**
	 * 成功处理
	 * @param obj 校验对象
	 * @param optional 交易数据
	 * @param r 处理结果
	 * @author lingting 2021-01-05 11:19
	 */
	public abstract void success(T obj, @NotNull Optional<Transaction> optional, R r);

	/**
	 * 异常处理
	 * @param obj 校验对象
	 * @param e 异常信息
	 * @author lingting 2021-01-05 11:29
	 */
	public abstract void error(T obj, Throwable e);

	@Override
	public void receiveProcess(List<T> list, T t) {
		try {
			// 收到就处理. 不再汇总处理
			handler(t, getTransaction(t));
		}
		catch (Exception e) {
			error(t, e);
		}
	}

	@Override
	public void process(List<T> list) throws Exception {
	}

}
