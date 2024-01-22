/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.pay.viratual;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import live.lingting.virtual.currency.core.model.TransactionInfo;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.redis.thread.AbstractRedisThread;

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
	 */
	public abstract Optional<TransactionInfo> getTransaction(T obj);

	/**
	 * 处理交易
	 * @param obj 校验对象信息
	 * @param optional 交易数据
	 */
	public abstract void handler(T obj, @NotNull Optional<TransactionInfo> optional);

	/**
	 * 失败处理
	 * @param obj 校验对象
	 * @param optional 交易数据
	 * @param r 处理结果
	 */
	public abstract void failed(T obj, @NotNull Optional<TransactionInfo> optional, R r);

	/**
	 * 成功处理
	 * @param obj 校验对象
	 * @param optional 交易数据
	 * @param r 处理结果
	 */
	public abstract void success(T obj, @NotNull Optional<TransactionInfo> optional, R r);

	/**
	 * 异常处理
	 * @param obj 校验对象
	 * @param e 异常信息
	 */
	public abstract void error(T obj, Throwable e);

	@Override
	public void receiveProcess(List<T> list, T t) {
		try {
			if (isRun()) {
				// 收到就处理. 不再汇总处理. 减少数据丢失的问题
				handler(t, getTransaction(t));
			}
			else {
				// 结束运行插入redis
				put(t);
			}
		}
		catch (Exception e) {
			error(t, e);
		}
	}

	@Override
	public void process(List<T> list) throws Exception {
	}

}
