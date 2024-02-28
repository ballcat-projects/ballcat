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
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.impl.MethodJobHandler;
import org.slf4j.MDC;

/**
 * 可追踪的 MethodJobHandler.
 *
 * @author Hccake
 * @see MethodJobHandler
 */
public class TraceMethodJobHandler extends IJobHandler {

	private final static String TRACE_ID = "traceId";

	private final MethodJobHandler methodJobHandler;

	public TraceMethodJobHandler(MethodJobHandler target) {
		this.methodJobHandler = target;
	}

	@Override
	public void execute() throws Exception {
		MDC.put(TRACE_ID, generateTraceId());
		try {
			this.methodJobHandler.execute();
		}
		finally {
			MDC.remove(TRACE_ID);
		}
	}

	@Override
	public void init() throws Exception {
		this.methodJobHandler.init();
	}

	@Override
	public void destroy() throws Exception {
		this.methodJobHandler.destroy();
	}

	@Override
	public String toString() {
		return this.methodJobHandler.toString();
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
