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
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 周期间隔Job
 *
 * @author evil0th Create on 2024/5/8
 */
@RequiredArgsConstructor
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DemoJob extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoJob.class);

	@Override
	public void executeInternal(JobExecutionContext context) {
		JobDataMap params = context.getMergedJobDataMap();
		String jobType = params.getString("jobType");
		long paramLong = params.getLong("paramLong");
		boolean paramBoolean = params.getBooleanValue("paramBoolean");
		LOGGER.info("准备执行Job,参数jobType={},long={},boolean={}", jobType, paramLong, paramBoolean);
	}

}
