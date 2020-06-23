package com.hccake.extend.kafka.stream.util;

import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.internals.ProcessorContextImpl;

/**
 * kafka 上下文工具类
 *
 * @author lingting  2020/6/23 18:00
 */
public class ProcessorContextUtil {

	public static String toLogString(ProcessorContext context) {
		String res = " 节点属性: application-id: " + context.applicationId();
		if (context instanceof ProcessorContextImpl) {
			res += " currentNode.name: " + ((ProcessorContextImpl) context).currentNode().name();
		}
		res += " topic: " + context.topic() +
				" offset: " + context.offset() +
				" taskId: " + context.taskId();
		return res;
	}
}
