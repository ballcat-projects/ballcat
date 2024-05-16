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

package org.ballcat.autoconfigure.quartz.service;

import java.util.Date;
import java.util.Map;

import org.quartz.DateBuilder;
import org.quartz.Job;

/**
 * quartz service
 *
 * @author evil0th Create on 2024/5/8
 */
public interface QuartzJobService {

	/**
	 * 以指定计划时间的方式新增一个任务
	 * @param clazz 任务实现类
	 * @param name 任务名称
	 * @param group 任务分组
	 * @param params 任务参数
	 * @param scheduleTime 指定schedule时间执行
	 * @param desc 任务描述
	 * @return 最近一次任务执行的开始时间
	 */
	Date addJob(Class<? extends Job> clazz, String name, String group, Map<String, Object> params, Date scheduleTime,
			String desc);

	/**
	 * 以指定未来多少时间的方式新增一个任务
	 * @param clazz 任务实现类
	 * @param name 任务名称
	 * @param group 任务分组
	 * @param params 任务参数
	 * @param future 未来多少时间
	 * @param unit 时间单位
	 * @param desc 任务描述
	 * @return 最近一次任务执行的开始时间
	 */
	Date addJob(Class<? extends Job> clazz, String name, String group, Map<String, Object> params, int future,
			DateBuilder.IntervalUnit unit, String desc);

	/**
	 * 以指定未来多少秒的方式新增一个任务
	 * @param clazz 任务实现类
	 * @param name 任务名称
	 * @param group 任务分组
	 * @param params 任务参数
	 * @param future 未来多少时间
	 * @param desc 任务描述
	 * @return 最近一次任务执行的开始时间
	 */
	Date addJob(Class<? extends Job> clazz, String name, String group, Map<String, Object> params, int future,
			String desc);

	/**
	 * 以cron的方式新增一个任务
	 * @param clazz 任务实现类
	 * @param name 任务名称
	 * @param group 任务分组
	 * @param params 任务参数
	 * @param cronExpression CRON表达式
	 * @param desc 任务描述
	 * @return 最近一次任务执行的开始时间
	 */
	Date addJob(Class<? extends Job> clazz, String name, String group, Map<String, Object> params,
			String cronExpression, String desc);

	/**
	 * 以cron的方式新增一个任务并指定结束时间
	 * @param clazz 任务实现类
	 * @param name 任务名称
	 * @param group 任务分组
	 * @param params 任务参数
	 * @param cronExpression CRON表达式
	 * @param desc 任务描述
	 * @param endTime 任务结束时间
	 * @return 最近一次任务执行的开始时间
	 */
	Date addJob(Class<? extends Job> clazz, String name, String group, Map<String, Object> params,
			String cronExpression, String desc, Date endTime);

	/**
	 * 添加任务每隔多少秒执行一次
	 * @param clazz 任务实现类
	 * @param name 任务名称
	 * @param group 任务分组
	 * @param params 任务参数
	 * @param desc 任务描述
	 * @param startTime 任务开始时间
	 * @param endTime 任务结束时间
	 * @param interval 间隔秒数
	 * @return 最近一次任务执行的开始时间
	 */
	Date addJobWithIntervalInSeconds(Class<? extends Job> clazz, String name, String group, Map<String, Object> params,
			String desc, Date startTime, Date endTime, int interval);

	/**
	 * 删除任务
	 * @param name 任务名称
	 * @param group 任务分组
	 * @return 是否删除
	 */
	boolean deleteJob(String name, String group);

	/**
	 * 是否存在Job
	 * @param name 任务名称
	 * @param group 任务分组
	 * @return 是否存在
	 */
	boolean existsJob(String name, String group);

	/**
	 * 更改任务参数、计划执行时间
	 * @param name 任务名称
	 * @param group 任务分组
	 * @param params 任务参数
	 * @param scheduleTime 指定呢计划执行时间
	 * @return 最近一次任务执行的开始时间
	 */
	Date modifyJob(String name, String group, Map<String, Object> params, Date scheduleTime);

	/**
	 * 删除任务组
	 * @param group 任务分组
	 */
	void deleteGroupJobs(String group);

}
