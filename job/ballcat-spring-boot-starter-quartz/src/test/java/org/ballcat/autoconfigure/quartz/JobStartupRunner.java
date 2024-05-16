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

package org.ballcat.autoconfigure.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.ballcat.autoconfigure.quartz.job.DemoJob;
import org.ballcat.autoconfigure.quartz.service.QuartzJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化job
 *
 * @author evil0th Create on 2024/5/8
 */
@RequiredArgsConstructor
@Component
public class JobStartupRunner implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobStartupRunner.class);

	final QuartzJobService quartzJobService;

	@Override
	public void run(String... args) {
		initIntervalJob();
		initCronJob();
		initOneTimeJob();
	}

	/**
	 * 初始化周期性定时任务
	 */
	public void initIntervalJob() {
		int interval = 30;
		Map<String, Object> params = new HashMap<>();
		params.put("jobType", "interval");
		params.put("paramLong", 1L);
		params.put("paramBoolean", Boolean.TRUE);
		Date latest = this.quartzJobService.addJobWithIntervalInSeconds(DemoJob.class, "DemoIntervalJob",
				"DemoJobGroup", params, String.format("每%d秒执行Job", interval), new Date(), null, interval);
		LOGGER.info("[周期间隔Job]初始化完成,每隔{}秒执行一次,下一次任务执行的开始时间{}", interval, latest);
	}

	/**
	 * 初始化cron定时任务
	 */
	public void initCronJob() {
		String cron = "0 0/1 * * * ?";
		Map<String, Object> params = new HashMap<>();
		params.put("jobType", "cron");
		params.put("paramLong", 2L);
		params.put("paramBoolean", Boolean.TRUE);
		Date latest = this.quartzJobService.addJob(DemoJob.class, "DemoCronJob", "DemoJobGroup", params, cron,
				String.format("Cron[%s] Job", cron));
		LOGGER.info("[Cron Job]初始化完成,cron:{},下一次任务执行的开始时间{}", cron, latest);
	}

	/**
	 * 初始化一次性任务
	 */
	private void initOneTimeJob() {
		Map<String, Object> params = new HashMap<>();
		params.put("jobType", "OneTime");
		params.put("paramLong", 3L);
		params.put("paramBoolean", Boolean.TRUE);
		Date latest = this.quartzJobService.addJob(DemoJob.class, "DemoOneTimeJob", "DemoJobGroup", params, 5,
				"一次性Job");
		LOGGER.info("[一次性Job]初始化完成,下一次任务执行的开始时间{}", latest);
	}

}
