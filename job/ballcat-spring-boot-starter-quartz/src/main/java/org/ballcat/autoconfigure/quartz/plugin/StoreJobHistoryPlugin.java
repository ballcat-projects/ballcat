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

package org.ballcat.autoconfigure.quartz.plugin;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import org.ballcat.autoconfigure.quartz.constants.JobConstant;
import org.ballcat.autoconfigure.quartz.enums.JobExecuteTraceType;
import org.ballcat.autoconfigure.quartz.enums.JobStage;
import org.ballcat.autoconfigure.quartz.event.JobExecuteTraceEvent;
import org.ballcat.autoconfigure.quartz.store.JobExecuteTrace;
import org.ballcat.common.util.SpringUtils;
import org.quartz.CalendarIntervalTrigger;
import org.quartz.CronTrigger;
import org.quartz.DailyTimeIntervalTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 保存任务历史记录
 *
 * @author evil0th Create on 2024/5/8
 */

@Setter
@Getter
public class StoreJobHistoryPlugin implements SchedulerPlugin, JobListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(StoreJobHistoryPlugin.class);

	/**
	 * 清理历史执行记录的Cron表达式。只是为了properties接收插件配置，无其他作用。
	 */
	private String cleanCron;

	/**
	 * 历史执行记录保留天数。只是为了properties接收插件配置，无其他作用。
	 */
	private String cleanDays;

	@Override
	public String getName() {
		return JobConstant.DEFAULT_STORE_JOB_HISTORY_PLUGIN_NAME;
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		JobDetail jobDetail = context.getJobDetail();
		JobKey jobKey = jobDetail.getKey();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[自定义插件:{}][{}]开始保存准备执行记录.", getName(), jobKey.getName());
		}
		try {
			triggerLog(context, null, JobExecuteTraceType.INITIALIZE);
		}
		catch (Exception e) {
			LOGGER.error("[自定义插件:{}][{}]保存准备执行记录失败.", getName(), jobKey.getName(), e);
		}
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {

	}

	/**
	 * 执行完自定义插件
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		JobDetail jobDetail = context.getJobDetail();
		JobKey jobKey = jobDetail.getKey();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[自定义插件:{}][{}]开始保存执行完毕记录.", getName(), jobKey.getName());
		}
		try {
			triggerLog(context, jobException, JobExecuteTraceType.UPDATE);
		}
		catch (Exception e) {
			LOGGER.error("[自定义插件:{}][{}]保存执行完毕记录失败.", getName(), jobKey.getName(), e);
		}
	}

	/**
	 * 初始化自定义插件
	 */
	@Override
	public void initialize(String name, Scheduler scheduler, ClassLoadHelper loadHelper) throws SchedulerException {
		scheduler.getListenerManager().addJobListener(this, EverythingMatcher.allJobs());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[自定义插件:{}]初始化.", getName());
		}
	}

	/**
	 * 启动自定义插件
	 */
	@Override
	public void start() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[自定义插件:{}]启动.", getName());
		}
	}

	/**
	 * 关闭自定义插件
	 */
	@Override
	public void shutdown() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[自定义插件:{}]关闭.", getName());
		}
	}

	private void triggerLog(JobExecutionContext context, JobExecutionException jobException,
			JobExecuteTraceType traceType) {
		Trigger trigger = context.getTrigger();
		JobDetail jobDetail = context.getJobDetail();
		JobKey jobKey = jobDetail.getKey();
		JobExecuteTrace trace = new JobExecuteTrace().setJobGroup(jobKey.getGroup())
			.setJobName(jobKey.getName())
			.setJobDescription(jobDetail.getDescription())
			.setTriggerType(trigger.getClass().getSimpleName())
			.setJobStrategy(getTriggerStrategy(trigger))
			.setJobClass(jobDetail.getJobClass().getName())
			.setJobParams(getJobDataMapJsonString(trigger.getJobDataMap()))
			.setExecuteTime(convertDate(context.getFireTime()))
			.setInstanceName(context.getFireInstanceId())
			.setStartTime(convertDate(trigger.getStartTime()))
			.setEndTime(convertDate(trigger.getEndTime()))
			.setPreviousFireTime(convertDate(trigger.getPreviousFireTime()))
			.setNextFireTime(convertDate(trigger.getNextFireTime()));
		switch (traceType) {
			case INITIALIZE:
				trace.setExecuteState(JobStage.EXECUTING.name());
				break;
			case UPDATE:
				trace.setRunTime(context.getJobRunTime())
					.setRetryTimes(context.getRefireCount())
					.setExecuteState(jobException == null ? JobStage.FINISHED.name() : JobStage.EXCEPTION.name())
					.setMessage(jobException != null ? jobException.getMessage() : null);
				break;
		}
		SpringUtils.publishEvent(new JobExecuteTraceEvent(trace, traceType));
	}

	/**
	 * @param trigger 触发器
	 * @return string
	 * @see SimpleTrigger SimpleTrigger:简单触发器 固定时刻或时间间隔，毫秒
	 * @see CalendarIntervalTrigger CalendarIntervalTrigger:基于日历的触发器
	 * 比简单触发器更多时间单位，支持非固定时间的触发，例如一年可能 365/366，一个月可能 28/29/30/31
	 * @see DailyTimeIntervalTrigger DailyTimeIntervalTrigger:基于日期的触发器 每天的某个时间段
	 * @see CronTrigger CronTrigger:基于 Cron 表达式的触发器
	 */
	private String getTriggerStrategy(Trigger trigger) {
		if (null == trigger) {
			return null;
		}
		if (trigger instanceof SimpleTriggerImpl) {
			// 这里也不是绝对的，依赖于QuartzService在创建未来时间执行使用的API
			SimpleTriggerImpl simpleTrigger = (SimpleTriggerImpl) trigger;
			final LocalDateTime localDateTime = convertDate(simpleTrigger.getStartTime());
			return localDateTime == null ? null : localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		}
		else if (trigger instanceof CronTrigger) {
			return ((CronTrigger) trigger).getCronExpression();
		}
		else if (trigger instanceof DailyTimeIntervalTrigger) {
			DailyTimeIntervalTrigger intervalTrigger = (DailyTimeIntervalTrigger) trigger;
			return String.format("%d/%s", intervalTrigger.getRepeatInterval(), intervalTrigger.getRepeatIntervalUnit());
		}
		else if (trigger instanceof CalendarIntervalTrigger) {
			CalendarIntervalTrigger intervalTrigger = (CalendarIntervalTrigger) trigger;
			return String.format("%d/%s", intervalTrigger.getRepeatInterval(), intervalTrigger.getRepeatIntervalUnit());
		}
		return null;
	}

	private LocalDateTime convertDate(Date dateToConvert) {
		if (null == dateToConvert) {
			return null;
		}
		return LocalDateTime.ofInstant(dateToConvert.toInstant(), ZoneId.systemDefault());
	}

	private String getJobDataMapJsonString(JobDataMap map) {
		if (null == map) {
			return null;
		}
		return map.keySet().stream().map(key -> key + ":" + map.get(key)).collect(Collectors.joining(",", "{", "}"));
	}

}
