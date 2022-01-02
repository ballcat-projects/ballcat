package com.hccake.extend.kafka.stream.extend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.extend.kafka.stream.core.AbstractPunctuator;
import com.hccake.extend.kafka.stream.exception.NotAllowedException;
import com.hccake.extend.kafka.stream.store.KafkaKeyValueStore;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueIterator;

/**
 * kafka 扩展类 自动注入 指定类型 指定名称的 store Value 数据的类型 Values 存放数据的对象类型
 *
 * @author lingting 2020/6/19 10:21
 */
@Slf4j
public abstract class AbstractKeyValueStorePunctuator<K, V, R> extends AbstractPunctuator {

	public static final int HANDLER_SIZE = 1000;

	@Getter
	private KafkaKeyValueStore<K, V> store;

	/**
	 * 用来处理单个值得函数
	 */
	private BiFunction<K, V, R> signHandle;

	/**
	 * 不允许使用这个初始化方法.
	 */
	@Override
	public AbstractKeyValueStorePunctuator<K, V, R> init(ProcessorContext context) {
		throw new NotAllowedException(
				"继承自 com.hccake.extend.kafka.stream.extend.AbstractKeyValueStorePunctuator 的类禁止使用当前方法进行初始化!");
	}

	public AbstractKeyValueStorePunctuator<K, V, R> init(ProcessorContext context, String storeName,
			BiFunction<K, V, R> signHandle) {
		super.init(context);
		store = getStore(storeName);
		this.signHandle = signHandle;
		return this;
	}

	/**
	 * 获取单批量处理数量
	 *
	 * @author lingting 2020-06-22 15:37:10
	 */
	public long getHandleSize() {
		return HANDLER_SIZE;
	}

	@Override
	public void handle(long timestamp) {
		KeyValueIterator<K, V> iterator = store.all();
		List<R> list = new ArrayList<>();

		while (iterator.hasNext()) {
			if (list.size() == getHandleSize()) {
				runHandle(timestamp, list);
				list.clear();
			}
			KeyValue<K, V> kv = iterator.next();
			list.add(signHandle.apply(kv.key, kv.value));
			store.delete(kv.key);
		}
		runHandle(timestamp, list);
	}

	/**
	 * 执行数据处理方法
	 * @param list 数据
	 * @param timestamp 时间戳
	 * @author lingting 2020-06-22 19:51:56
	 */
	public void runHandle(long timestamp, List<R> list) {
		try {
			if (!list.isEmpty()) {
				log.debug("任务执行中,类名 {}, 操作数据量: {}", this.getClass().getSimpleName(), list.size());
				handle(timestamp, list);
			}
		}
		catch (Exception e) {
			errLog(e);
			try {
				log.error("时间戳: {}, 类名: {}, 异常数据: {}", timestamp, this.getClass().getName(),
						new ObjectMapper().writeValueAsString(list));
			}
			catch (Exception exception) {
				log.error("记录异常数据出错! 时间戳: {}, 类名: {}", timestamp, this.getClass().getName());
				log.error("数据转换异常! ", exception);
			}
		}
	}

	/**
	 * 批量处理数据
	 * @param timestamp 时间戳
	 * @param list 当前批数据
	 * @author lingting 2020-06-22 15:35:38
	 */
	public abstract void handle(long timestamp, List<R> list);

	@Override
	public void clean() {

	}

}
