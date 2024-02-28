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

package org.ballcat.autoconfigure.xxljob.trace;

import java.util.UUID;

import com.xxl.job.core.context.XxlJobHelper;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.MDC;

/**
 * 增强 XxlJob Trace 能力的拦截器，在 jobHandler execute 前在 MDC 中置入 traceId, 方便追踪上下文日志。
 *
 * @author Hccake
 */
public class TraceXxlJobAnnotationInterceptor implements MethodInterceptor {

	private final static String TRACE_ID = "traceId";

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		MDC.put(TRACE_ID, generateTraceId());
		try {
			return invocation.proceed();
		}
		finally {
			MDC.remove(TRACE_ID);
		}
	}

	/**
	 * 生成 traceId, 默认使用 jobId 拼接无下划线的 UUID
	 * @return traceId
	 */
	protected String generateTraceId() {
		long jobId = XxlJobHelper.getJobId();
		String simpleUUID = UUID.randomUUID().toString().replace("-", "");
		return jobId + "-" + simpleUUID;
	}

}
