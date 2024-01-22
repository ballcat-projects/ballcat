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

import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.To;
import org.ballcat.kafka.stream.util.ProcessorContextUtil;

/**
 * kafka 顶级 processor 类
 *
 * @author lingting 2020/6/16 22:27
 */
@Slf4j
public abstract class AbstractProcessor<K, V> implements Kafka, Processor<K, V> {

	@Getter
	private ProcessorContext context;

	@Override
	public void init(ProcessorContext context) {
		this.context = context;
		initSchedule(this.context);
	}

	/**
	 * 用于初始化窗口的方法 子类如果需要 自己重写
	 */
	public void initSchedule(ProcessorContext context) {

	}

	/**
	 * 用于构筑 Punctuator
	 */
	public void schedule(Duration interval, PunctuationType type, AbstractPunctuator callback) {
		this.context.schedule(interval, type, callback);
	}

	/**
	 * 用于构筑 {@link PunctuationType#WALL_CLOCK_TIME} 类型的 Punctuator
	 */
	public void schedule(Duration interval, AbstractPunctuator callback) {
		schedule(interval, PunctuationType.WALL_CLOCK_TIME, callback);
	}

	/**
	 * 下发数据
	 * @param key key
	 * @param value value
	 * @param childName 目标名称
	 */
	public void forward(K key, V value, String childName) {
		this.context.forward(key, value, To.child(childName));
	}

	/**
	 * 下发数据
	 * @param key key
	 * @param value value
	 * @param to 目标
	 */
	public void forward(K key, V value, To to) {
		this.context.forward(key, value, to);
	}

	public void startLog(K key, V value) {
		log.debug("收到消息 {}  key: {} value: {}", ProcessorContextUtil.toLogString(this.context), key, value);
	}

	public void errLog(Throwable e) {
		log.error("processor 操作数据出错 " + ProcessorContextUtil.toLogString(this.context), e);
	}

	@Override
	public void process(K key, V value) {
		// 由于测试中存在 处理过程报错，整个 topology 停止运行，所以直接捕获异常
		try {
			startLog(key, value);
			process(this.context, key, value);
		}
		catch (Exception e) {
			errLog(e);
			String errStr = null;
			try {
				errStr = new ObjectMapper().writeValueAsString(value);
			}
			catch (Exception ex) {
				log.error("数据转json出错!msg: {}", ex.getMessage());
				// 多余的转换? errStr = Convert.toStr(value);
			}
			log.error("异常数据 {}", errStr);
		}
	}

	/**
	 * Process the record with the given key and value.
	 * @param context 上下文
	 * @param key the key for the record
	 * @param value the value for the record
	 */
	public abstract void process(ProcessorContext context, K key, V value);

	/**
	 * 子类需要时， 自己重写
	 */
	@Override
	public void close() {
	}

}
