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

package org.ballcat.autoconfigure.xxljob;

import java.util.Objects;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.autoconfigure.xxljob.properties.XxlExecutorProperties;
import org.ballcat.autoconfigure.xxljob.properties.XxlJobProperties;
import org.ballcat.autoconfigure.xxljob.trace.TraceXxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * xxl 初始化
 *
 * @author lengleng 2019-09-18
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnProperty(prefix = XxlJobProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class XxlJobAutoConfiguration {

	private final Environment environment;

	@Bean
	public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties xxlJobProperties) {
		if (log.isInfoEnabled()) {
			log.info(">>>>>>>>>>> xxl-job config init.");
		}
		XxlJobSpringExecutor xxlJobSpringExecutor = new TraceXxlJobSpringExecutor();
		xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
		XxlExecutorProperties executorProperties = xxlJobProperties.getExecutor();
		xxlJobSpringExecutor.setAppname(getExecutorName(executorProperties));
		xxlJobSpringExecutor.setIp(executorProperties.getIp());
		xxlJobSpringExecutor.setPort(executorProperties.getPort());
		xxlJobSpringExecutor.setAccessToken(getAccessToken(xxlJobProperties));
		xxlJobSpringExecutor.setLogPath(getLogPath(executorProperties));
		xxlJobSpringExecutor.setLogRetentionDays(executorProperties.getLogRetentionDays());
		xxlJobSpringExecutor.setAddress(executorProperties.getAddress());
		return xxlJobSpringExecutor;
	}

	/**
	 * 获取执行器名称，缺省为Spring Boot应用名称
	 * @param properties 执行器配置
	 * @return 执行期名称
	 */
	private String getExecutorName(XxlExecutorProperties properties) {
		String appName = properties.getAppname();
		if (StringUtils.hasText(appName)) {
			return appName;
		}
		else {
			return this.environment.getProperty("spring.application.name");
		}
	}

	/**
	 * 获取xxl-job执行器通讯令牌
	 * @param properties 主配置文件
	 * @return accessToken
	 */
	private String getAccessToken(XxlJobProperties properties) {
		if (StringUtils.hasText(properties.getAccessToken())) {
			return properties.getAccessToken();
		}
		else {
			log.warn("为提升系统安全性，生产环境建议启用调度中心和执行器安全性校验！可通过配置项 “xxl.job.accessToken” 进行AccessToken的设置");
			return null;
		}
	}

	/**
	 * 获取日志路径
	 * @param properties 执行器配置文件
	 * @return log path
	 */
	private String getLogPath(XxlExecutorProperties properties) {
		String logPath = properties.getLogPath();
		if (StringUtils.hasText(logPath)) {
			return logPath;
		}
		return this.environment.getProperty("logging.file.path", "logs")
			.concat("/")
			.concat(Objects.requireNonNull(this.environment.getProperty("spring.application.name")))
			.concat("/jobs");
	}

}
