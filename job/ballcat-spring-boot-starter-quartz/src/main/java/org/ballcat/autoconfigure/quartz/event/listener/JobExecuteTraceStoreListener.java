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

package org.ballcat.autoconfigure.quartz.event.listener;

import lombok.RequiredArgsConstructor;
import org.ballcat.autoconfigure.quartz.event.JobExecuteTraceEvent;
import org.ballcat.autoconfigure.quartz.store.JobExecuteTraceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * 任务执行记录事件监听
 *
 * @author evil0th Create on 2024/5/8
 */
@RequiredArgsConstructor
public class JobExecuteTraceStoreListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobExecuteTraceStoreListener.class);

	private final JobExecuteTraceStore jobExecuteTraceStore;

	/**
	 * 处理异步保存任务执行记录事件
	 * @param event {@link JobExecuteTraceEvent}
	 */
	@Async
	@EventListener(JobExecuteTraceEvent.class)
	public void handleJobExecuteTraceEvent(JobExecuteTraceEvent event) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("接收到异步存储任务执行记录事件{}，处理中...", event);
		}
		switch (event.getEventType()) {
			case UPDATE:
			case INITIALIZE:
				this.jobExecuteTraceStore.storeTrace(event.getJobExecuteTrace());
				break;
			default:
				LOGGER.error("未知的事件类型:{}", event.getEventType());
		}
	}

}
