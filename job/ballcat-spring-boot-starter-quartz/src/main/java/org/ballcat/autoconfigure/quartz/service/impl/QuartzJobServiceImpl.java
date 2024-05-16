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

package org.ballcat.autoconfigure.quartz.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.ballcat.autoconfigure.quartz.service.QuartzJobService;
import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * quartz service impl
 *
 * @author evil0th Create on 2024/5/8
 */
@RequiredArgsConstructor
public class QuartzJobServiceImpl implements QuartzJobService {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzJobServiceImpl.class);

	/**
	 * 优先级在此维护
	 * <p>
	 * 较高优先级的触发器先触发。如果没有为trigger设置优先级，trigger使用默认优先级，值为5；
	 */
	public static final int PRIORITY_DEFAULT = 5;

	public static final String CRON_FORMAT = "ss mm HH dd MM ? yyyy";

	final Scheduler scheduler;

	@Override
	public Date addJob(Class<? extends Job> clazz, String name, String group, Map<String, Object> params,
			Date scheduleTime, String desc) {
		try {
			// 先检查任务是否存在,存在则删除
			JobKey jobKey = JobKey.jobKey(name, group);
			if (this.scheduler.checkExists(jobKey)) {
				deleteJob(name, group);
			}
			JobDetail job = JobBuilder.newJob(clazz).withDescription(desc).withIdentity(jobKey).build();
			if (params == null) {
				params = new HashMap<>();
			}
			JobDataMap dataMap = new JobDataMap(params);
			TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
			// 创建触发器
			SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
				.withMisfireHandlingInstructionFireNow();
			TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger()
				.withIdentity(triggerKey)
				.usingJobData(dataMap)
				.withDescription(desc)
				.startAt(scheduleTime)
				.withPriority(PRIORITY_DEFAULT);
			return this.scheduler.scheduleJob(job, builder.withSchedule(simpleScheduleBuilder).build());
		}
		catch (Exception e) {
			LOGGER.error("以指定计划时间的方式新增任务失败!", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Date addJob(Class<? extends Job> clazz, String name, String group, Map<String, Object> params, int future,
			DateBuilder.IntervalUnit unit, String desc) {
		try {
			// 先检查任务是否存在,存在则删除
			JobKey jobKey = JobKey.jobKey(name, group);
			if (this.scheduler.checkExists(jobKey)) {
				deleteJob(name, group);
			}
			JobDetail job = JobBuilder.newJob(clazz).withDescription(desc).withIdentity(jobKey).build();
			if (params == null) {
				params = new HashMap<>();
			}
			JobDataMap dataMap = new JobDataMap(params);
			TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
			// 创建触发器
			SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
				.withMisfireHandlingInstructionFireNow();
			TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger()
				.withIdentity(triggerKey)
				.usingJobData(dataMap)
				.withDescription(desc)
				.startAt(DateBuilder.futureDate(future, null == unit ? DateBuilder.IntervalUnit.SECOND : unit))
				.withPriority(PRIORITY_DEFAULT);
			return this.scheduler.scheduleJob(job, builder.withSchedule(simpleScheduleBuilder).build());
		}
		catch (Exception e) {
			LOGGER.error("以指定计划时间的方式新增任务失败!", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Date addJob(Class<? extends Job> clazz, String name, String group, Map<String, Object> params, int future,
			String desc) {
		return addJob(clazz, name, group, params, future, DateBuilder.IntervalUnit.SECOND, desc);
	}

	@Override
	public Date addJob(Class<? extends Job> clazz, String name, String group, Map<String, Object> params,
			String cronExpression, String desc) {
		return addJob(clazz, name, group, params, cronExpression, desc, new Date(), null);
	}

	@Override
	public Date addJob(Class<? extends Job> clazz, String name, String group, Map<String, Object> params,
			String cronExpression, String desc, Date endTime) {
		return addJob(clazz, name, group, params, cronExpression, desc, new Date(), endTime);
	}

	public Date addJob(Class<? extends Job> clazz, String name, String group, Map<String, Object> params,
			String cronExpression, String desc, Date startTime, Date endTime) {
		try {
			// 先检查任务是否存在,存在则删除
			JobKey jobKey = JobKey.jobKey(name, group);
			if (this.scheduler.checkExists(jobKey)) {
				deleteJob(name, group);
			}
			JobDetail job = JobBuilder.newJob(clazz).withDescription(desc).withIdentity(jobKey).build();
			if (params == null) {
				params = new HashMap<>();
			}
			JobDataMap dataMap = new JobDataMap(params);
			TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
			// 创建触发器
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
				.withMisfireHandlingInstructionFireAndProceed();
			TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger()
				.withIdentity(triggerKey)
				.usingJobData(dataMap)
				.withDescription(desc)
				.startAt(startTime)
				.withPriority(PRIORITY_DEFAULT);
			if (endTime != null) {
				builder = builder.endAt(endTime);
			}
			return this.scheduler.scheduleJob(job, builder.withSchedule(cronScheduleBuilder).build());
		}
		catch (Exception e) {
			LOGGER.error("以cron的方式新增任务失败!", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Date addJobWithIntervalInSeconds(Class<? extends Job> clazz, String name, String group,
			Map<String, Object> params, String desc, Date startTime, Date endTime, int interval) {
		try {
			// 先检查任务是否存在,存在则删除
			JobKey jobKey = JobKey.jobKey(name, group);
			if (this.scheduler.checkExists(jobKey)) {
				deleteJob(name, group);
			}
			JobDetail job = JobBuilder.newJob(clazz).withDescription(desc).withIdentity(jobKey).build();
			if (params == null) {
				params = new HashMap<>();
			}
			JobDataMap dataMap = new JobDataMap(params);
			TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
			// 创建触发器
			CalendarIntervalScheduleBuilder cisb = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
				.withIntervalInSeconds(interval)
				.withMisfireHandlingInstructionFireAndProceed();
			TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger()
				.withIdentity(triggerKey)
				.usingJobData(dataMap)
				.withDescription(desc)
				.startAt(startTime)
				.withPriority(PRIORITY_DEFAULT);
			if (endTime != null) {
				builder = builder.endAt(endTime);
			}
			return this.scheduler.scheduleJob(job, builder.withSchedule(cisb).build());
		}
		catch (Exception e) {
			LOGGER.error("以每隔多少秒执行一次方式新增任务失败!", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean deleteJob(String name, String group) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
			// 停止触发器
			this.scheduler.pauseTrigger(triggerKey);
			// 移除触发器
			this.scheduler.unscheduleJob(triggerKey);
			JobKey jobKey = JobKey.jobKey(name, group);
			// 删除任务
			return this.scheduler.deleteJob(jobKey);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean existsJob(String name, String group) {
		try {
			// 先检查任务是否存在,存在则删除
			JobKey jobKey = JobKey.jobKey(name, group);
			return this.scheduler.checkExists(jobKey);
		}
		catch (Exception e) {
			LOGGER.error("检测任务出错错误！", e);
		}
		return false;
	}

	@Override
	public Date modifyJob(String name, String group, Map<String, Object> params, Date scheduleTime) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
			CronTrigger trigger = (CronTrigger) this.scheduler.getTrigger(triggerKey);
			if (trigger == null) {
				return null;
			}
			String oldTime = trigger.getCronExpression();
			String newTime = LocalDateTime.ofInstant(scheduleTime.toInstant(), ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern(CRON_FORMAT));
			if (!newTime.equals(oldTime)) {
				JobKey jobKey = JobKey.jobKey(name, group);
				JobDetail jobDetail = this.scheduler.getJobDetail(jobKey);
				if (jobDetail != null) {
					// 修改时间
					if (deleteJob(name, group)) {
						return addJob(jobDetail.getJobClass(), name, group, params, scheduleTime,
								jobDetail.getDescription());
					}
				}
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	/**
	 * 根据组名称删除任务组
	 * @param group 任务组
	 */
	@Override
	public void deleteGroupJobs(String group) {
		try {
			GroupMatcher<TriggerKey> triggerMatcher = GroupMatcher.groupEquals(group);
			Set<TriggerKey> triggerKeySet = this.scheduler.getTriggerKeys(triggerMatcher);
			List<TriggerKey> triggerKeyList = new ArrayList<>(triggerKeySet);
			this.scheduler.pauseTriggers(triggerMatcher);
			this.scheduler.unscheduleJobs(triggerKeyList);
			GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals(group);
			Set<JobKey> jobKeySet = this.scheduler.getJobKeys(matcher);
			List<JobKey> jobKeyList = new ArrayList<>(jobKeySet);
			this.scheduler.deleteJobs(jobKeyList);
			// 要恢复组触发器，否则以后添加的任务会处于暂停状态，无法运行
			this.scheduler.resumeTriggers(triggerMatcher);
		}
		catch (Exception e) {
			LOGGER.error("删除任务组失败!", e);
			throw new RuntimeException(e);
		}

	}

}
