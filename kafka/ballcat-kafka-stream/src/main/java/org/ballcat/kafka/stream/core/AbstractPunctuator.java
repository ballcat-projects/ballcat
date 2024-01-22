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

package org.ballcat.kafka.stream.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.Punctuator;
import org.ballcat.common.system.StopWatch;
import org.ballcat.kafka.stream.util.ProcessorContextUtil;

/**
 * kafka 顶级 punctuator 类
 *
 * @author lingting 2020/6/17 14:02
 */
@Slf4j
public abstract class AbstractPunctuator implements Kafka, Punctuator {

	@Getter
	private ProcessorContext context;

	public AbstractPunctuator init(ProcessorContext context) {
		this.context = context;
		return this;
	}

	/**
	 * 是否处理数据, 如果想依据条件执行，需要自己重写当前方法
	 * @return boolean true 表示有参数，可以执行处理
	 */
	public boolean isHandle() {
		return true;
	}

	/**
	 * 用于处理完数据后，清空当前存储的数据
	 */
	public abstract void clean();

	/**
	 * handle 开始日志
	 */
	public void startLog() {
		log.debug("任务开始执行, 类名 {} ,{}", this.getClass().getSimpleName(), ProcessorContextUtil.toLogString(this.context));
	}

	/**
	 * handle 结束日志
	 * @param time 执行时长 单位 毫秒
	 */
	public void endLog(long time) {
		log.debug("任务执行时长: {}, 类名 {}, {} ", time, this.getClass().getSimpleName(),
				ProcessorContextUtil.toLogString(this.context));
	}

	/**
	 * 异常日志
	 */
	public void errLog(Throwable e) {
		log.error("punctuator 操作数据出错 类名 " + this.getClass().getSimpleName() + ", "
				+ ProcessorContextUtil.toLogString(this.context), e);
	}

	@Override
	public void punctuate(long timestamp) {
		try {
			if (isHandle()) {
				StopWatch watch = new StopWatch();
				try {
					watch.start();
					startLog();
					handle(timestamp);
					endLog(watch.timeMillis());
					// 清除数据
					clean();
					this.context.commit();
				}
				finally {
					watch.stop();
				}
			}
		}
		catch (Exception e) {
			errLog(e);
		}
	}

	/**
	 * 处理聚合的数据
	 * @param timestamp 时间戳
	 */
	public abstract void handle(long timestamp);

}
