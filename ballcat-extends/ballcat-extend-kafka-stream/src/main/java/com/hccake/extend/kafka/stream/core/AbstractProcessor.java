package com.hccake.extend.kafka.stream.core;

import cn.hutool.core.convert.Convert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.extend.kafka.stream.util.ProcessorContextUtil;
import java.time.Duration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.To;

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
	 *
	 * @author lingting 2020-06-17 10:44:39
	 */
	public void initSchedule(ProcessorContext context) {

	}

	/**
	 * 用于构筑 Punctuator
	 *
	 * @author lingting 2020-06-21 13:58:34
	 */
	public void schedule(Duration interval, PunctuationType type, AbstractPunctuator callback) {
		context.schedule(interval, type, callback);
	}

	/**
	 * 用于构筑 {@link PunctuationType#WALL_CLOCK_TIME} 类型的 Punctuator
	 *
	 * @author lingting 2020-06-21 13:58:53
	 */
	public void schedule(Duration interval, AbstractPunctuator callback) {
		schedule(interval, PunctuationType.WALL_CLOCK_TIME, callback);
	}

	/**
	 * 下发数据
	 * @param key key
	 * @param value value
	 * @param childName 目标名称
	 * @author lingting 2020-06-17 19:44:45
	 */
	public void forward(K key, V value, String childName) {
		context.forward(key, value, To.child(childName));
	}

	/**
	 * 下发数据
	 * @param key key
	 * @param value value
	 * @param to 目标
	 * @author lingting 2020-06-17 19:47:55
	 */
	public void forward(K key, V value, To to) {
		context.forward(key, value, to);
	}

	public void startLog(K key, V value) {
		log.debug("收到消息 {}  key: {} value: {}", ProcessorContextUtil.toLogString(context), key, value);
	}

	public void errLog(Throwable e) {
		log.error("processor 操作数据出错 " + ProcessorContextUtil.toLogString(context), e);
	}

	@Override
	public void process(K key, V value) {
		// 由于测试中存在 处理过程报错，整个 topology 停止运行，所以直接捕获异常
		try {
			startLog(key, value);
			process(context, key, value);
		}
		catch (Exception e) {
			errLog(e);
			String errStr;
			try {
				errStr = new ObjectMapper().writeValueAsString(value);
			}
			catch (Exception ex) {
				log.error("数据转json出错!msg: {}", ex.getMessage());
				errStr = Convert.toStr(value);
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
