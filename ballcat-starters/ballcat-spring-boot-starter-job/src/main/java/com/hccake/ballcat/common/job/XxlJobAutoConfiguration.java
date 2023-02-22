package com.hccake.ballcat.common.job;

import com.hccake.ballcat.common.job.properties.XxlExecutorProperties;
import com.hccake.ballcat.common.job.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * @author lengleng
 * @date 2019-09-18
 * <p>
 * xxl 初始化
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
		XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
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
			return environment.getProperty("spring.application.name");
		}
	}

	/**
	 * 获取xxl-job执行器通讯令牌
	 * @param properties 主配置文件
	 * @return
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
	 * @return
	 */
	private String getLogPath(XxlExecutorProperties properties) {
		String logPath = properties.getLogPath();
		if (StringUtils.hasText(logPath)) {
			return logPath;
		}
		return environment.getProperty("logging.file.path", "logs")
			.concat("/")
			.concat(environment.getProperty("spring.application.name"))
			.concat("/jobs");
	}

}
