package com.hccake.extend.kafka.stream.core;

import cn.hutool.core.date.TimeInterval;
import com.hccake.extend.kafka.stream.util.ProcessorContextUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.Punctuator;

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
	 * @author lingting 2020-06-17 14:05:01
	 */
	public boolean isHandle() {
		return true;
	}

	/**
	 * 用于处理完数据后，清空当前存储的数据
	 *
	 * @author lingting 2020-06-17 15:55:44
	 */
	public abstract void clean();

	/**
	 * handle 开始日志
	 *
	 * @author lingting 2020-06-21 18:34:15
	 */
	public void startLog() {
		log.debug("任务开始执行, 类名 {} ,{}", this.getClass().getSimpleName(), ProcessorContextUtil.toLogString(context));
	}

	/**
	 * handle 结束日志
	 * @param time 执行时长 单位 毫秒
	 * @author lingting 2020-06-21 16:39:16
	 */
	public void endLog(long time) {
		log.debug("任务执行时长: {}, 类名 {}, {} ", time, this.getClass().getSimpleName(),
				ProcessorContextUtil.toLogString(context));
	}

	/**
	 * 异常日志
	 *
	 * @author lingting 2020-06-22 19:50:16
	 */
	public void errLog(Throwable e) {
		log.error("punctuator 操作数据出错 类名 " + this.getClass().getSimpleName() + ", "
				+ ProcessorContextUtil.toLogString(context), e);
	}

	@Override
	public void punctuate(long timestamp) {
		try {
			if (isHandle()) {
				TimeInterval interval = new TimeInterval();
				startLog();
				handle(timestamp);
				endLog(interval.intervalMs());
				// 清除数据
				clean();
				context.commit();
			}
		}
		catch (Exception e) {
			errLog(e);
		}
	}

	/**
	 * 处理聚合的数据
	 * @param timestamp 时间戳
	 * @author lingting 2020-06-17 14:06:25
	 */
	public abstract void handle(long timestamp);

}
