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

package org.ballcat.autoconfigure.quartz.event;

import lombok.Getter;
import lombok.Setter;
import org.ballcat.autoconfigure.quartz.enums.JobExecuteTraceType;
import org.ballcat.autoconfigure.quartz.store.JobExecuteTrace;
import org.springframework.context.ApplicationEvent;

/**
 * 任务执行记录事件
 *
 * @author evil0th Create on 2024/5/8
 */
@Getter
@Setter
public class JobExecuteTraceEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private final JobExecuteTrace jobExecuteTrace;

	private JobExecuteTraceType eventType;

	/**
	 * @param jobExecuteTrace JobExecuteTrace
	 * @param eventType {@link JobExecuteTraceType}
	 */
	public JobExecuteTraceEvent(JobExecuteTrace jobExecuteTrace, JobExecuteTraceType eventType) {
		super(jobExecuteTrace.getJobName());
		this.jobExecuteTrace = jobExecuteTrace;
		this.eventType = eventType;
	}

}
