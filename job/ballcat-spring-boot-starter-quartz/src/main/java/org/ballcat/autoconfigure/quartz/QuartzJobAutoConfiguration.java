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

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.autoconfigure.quartz.constants.JobConstant;
import org.ballcat.autoconfigure.quartz.event.listener.JobExecuteTraceStoreListener;
import org.ballcat.autoconfigure.quartz.job.CleanExecuteTraceJob;
import org.ballcat.autoconfigure.quartz.properties.QuartzJobProperties;
import org.ballcat.autoconfigure.quartz.service.QuartzJobService;
import org.ballcat.autoconfigure.quartz.service.impl.QuartzJobServiceImpl;
import org.ballcat.autoconfigure.quartz.store.JobExecuteTraceStore;
import org.ballcat.autoconfigure.quartz.store.impl.JdbcJobExecuteTraceStore;
import org.quartz.Scheduler;
import org.quartz.spi.SchedulerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * quartz 自动装配
 *
 * @author evil0th Create on 2024/5/8
 */
@Slf4j
@EnableAsync
@AutoConfiguration
@AutoConfigureAfter(QuartzAutoConfiguration.class)
@EnableConfigurationProperties(QuartzJobProperties.class)
@ConditionalOnProperty(prefix = QuartzJobProperties.PREFIX, name = "enabled", havingValue = "true",
		matchIfMissing = true)
public class QuartzJobAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzJobAutoConfiguration.class);

	@Bean
	public QuartzJobService quartzJobService(Scheduler scheduler) {
		return new QuartzJobServiceImpl(scheduler);
	}

	/**
	 * 配置了插件则响应存储组件要初始化 同时添加清理历史数据的任务
	 * 下游可以实现{@link JobExecuteTraceStore}接口和{@link SchedulerPlugin}来自定义存储
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnSingleCandidate(DataSource.class)
	@ConditionalOnProperty(prefix = "spring.quartz", name = "job-store-type", havingValue = "jdbc")
	static class JobExecuteTraceStoreConfiguration {

		/**
		 * 提供默认jdbc存储实现
		 */
		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(prefix = JobConstant.DEFAULT_STORE_JOB_HISTORY_PLUGIN_PROPERTIES_PREFIX, name = "class",
				havingValue = JobConstant.DEFAULT_STORE_JOB_HISTORY_PLUGIN_CLASS)
		public JobExecuteTraceStore jdbcJobExecuteTraceStore(DataSource dataSource) {
			return new JdbcJobExecuteTraceStore(dataSource);
		}

		/**
		 * 定制了清理 cron 配置
		 */
		@Bean
		@ConditionalOnBean(JobExecuteTraceStore.class)
		@ConditionalOnProperty(prefix = JobConstant.DEFAULT_STORE_JOB_HISTORY_PLUGIN_PROPERTIES_PREFIX,
				name = "cleanCron")
		public JobExecuteTraceStoreListener jobExecuteTraceStoreListener(JobExecuteTraceStore jobExecuteTraceStore,
				QuartzJobService quartzJobService, QuartzProperties quartzProperties) {
			// 按Cron表达式执行清理历史数据Job
			final String cron = quartzProperties.getProperties()
				.get(JobConstant.PLUGIN_PROPERTIES_PREFIX + "." + JobConstant.DEFAULT_STORE_JOB_HISTORY_PLUGIN_NAME
						+ ".cleanCron");
			// 默认清理30天以前的历史数据
			final int cleanDays = Integer.parseInt(quartzProperties.getProperties()
				.getOrDefault(JobConstant.PLUGIN_PROPERTIES_PREFIX + "."
						+ JobConstant.DEFAULT_STORE_JOB_HISTORY_PLUGIN_NAME + ".cleanDays", "30"));
			Map<String, Object> params = new HashMap<>();
			params.put("cleanDays", cleanDays);
			Date latest = quartzJobService.addJob(CleanExecuteTraceJob.class,
					JobConstant.DEFAULT_STORE_JOB_HISTORY_PLUGIN_NAME + "CleanJob", JobConstant.RESERVED_JOB_GROUP,
					params, cron, null);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("[定时清理任务{}天前历史记录]初始化, CRON={}, 下一次任务执行的开始时间{}", cleanDays, cron, latest);
			}
			return new JobExecuteTraceStoreListener(jobExecuteTraceStore);
		}

	}

}
