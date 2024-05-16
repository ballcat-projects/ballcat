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

package org.ballcat.autoconfigure.quartz.store.impl;

import java.sql.Types;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;
import org.ballcat.autoconfigure.quartz.store.JobExecuteTrace;
import org.ballcat.autoconfigure.quartz.store.JobExecuteTraceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * 默认jdbc存储实现
 *
 * <p>
 * 用户可自行实现性能更好的存储方式，如接入监控系统等
 * </p>
 *
 * @author evil0th Create on 2024/5/8
 */
@Getter
@Setter
public class JdbcJobExecuteTraceStore implements JobExecuteTraceStore {

	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcJobExecuteTraceStore.class);

	private static final String TABLE_NAME = "qrtz_job_execute_trace";

	private static final String DEFAULT_TRACE_INSERT_STATEMENT = "insert into " + TABLE_NAME
			+ "(`job_group`, `job_name`, `job_description`, `trigger_type`, "
			+ "`job_strategy`, `job_class`, `job_params`, `execute_time`, `execute_state`, "
			+ "`run_time`, `retry_times`, `previous_fire_time`, `next_fire_time`, "
			+ "`message`, `start_time`, `end_time`, `instance_name`) "
			+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String DEFAULT_HISTORY_TRACE_DELETE_STATEMENT = "delete from " + TABLE_NAME
			+ " where create_time < ?";

	private String insertTraceSql = DEFAULT_TRACE_INSERT_STATEMENT;

	private String deleteHistoryTraceSql = DEFAULT_HISTORY_TRACE_DELETE_STATEMENT;

	private final JdbcTemplate jdbcTemplate;

	private final DataSource dataSource;

	public JdbcJobExecuteTraceStore(DataSource dataSource) {
		Assert.notNull(dataSource, "DataSource required");
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void storeTrace(JobExecuteTrace t) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("storeTrace:{}", t);
		}
		this.jdbcTemplate.update(this.insertTraceSql,
				new Object[] { t.getJobGroup(), t.getJobName(), t.getJobDescription(), t.getTriggerType(),
						t.getJobStrategy(), t.getJobClass(), t.getJobParams(), t.getExecuteTime(), t.getExecuteState(),
						t.getRunTime(), t.getRetryTimes(), t.getPreviousFireTime(), t.getNextFireTime(), t.getMessage(),
						t.getStartTime(), t.getEndTime(), t.getInstanceName() },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.TIMESTAMP,
						Types.TIMESTAMP, Types.VARCHAR, Types.TIMESTAMP, Types.TIMESTAMP, Types.VARCHAR });

	}

	@Override
	public void cleanUp(int cleanDays) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info("清理{}天之前的任务历史记录 ...", cleanDays);
		}
		this.jdbcTemplate.update(this.deleteHistoryTraceSql, new Object[] { LocalDateTime.now().minusDays(cleanDays) },
				new int[] { Types.TIMESTAMP });
	}

}
