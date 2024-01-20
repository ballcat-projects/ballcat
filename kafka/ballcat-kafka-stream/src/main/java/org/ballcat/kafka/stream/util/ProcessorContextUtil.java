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

package org.ballcat.kafka.stream.util;

import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.internals.ProcessorContextImpl;

/**
 * kafka 上下文工具类
 *
 * @author lingting 2020/6/23 18:00
 */
public final class ProcessorContextUtil {

	private ProcessorContextUtil() {
	}

	public static String toLogString(ProcessorContext context) {
		String res = " 节点属性: application-id: " + context.applicationId();
		if (context instanceof ProcessorContextImpl) {
			res += " currentNode.name: " + ((ProcessorContextImpl) context).currentNode().name();
		}
		res += " topic: " + context.topic() + " offset: " + context.offset() + " taskId: " + context.taskId();
		return res;
	}

}
