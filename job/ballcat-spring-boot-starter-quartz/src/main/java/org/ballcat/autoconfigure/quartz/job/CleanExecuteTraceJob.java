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

package org.ballcat.autoconfigure.quartz.job;

import lombok.RequiredArgsConstructor;
import org.ballcat.autoconfigure.quartz.store.JobExecuteTraceStore;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 清理 {@link JobExecuteTraceStore} 中的记录的历史数据Job
 *
 * @author evil0th Create on 2024/5/8
 */
@RequiredArgsConstructor
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CleanExecuteTraceJob extends QuartzJobBean {

	final JobExecuteTraceStore jobExecuteTraceStore;

	@Override
	protected void executeInternal(@NonNull JobExecutionContext context) {
		JobDataMap params = context.getMergedJobDataMap();
		final int cleanDays = params.getInt("cleanDays");
		this.jobExecuteTraceStore.cleanUp(cleanDays);
	}

}
