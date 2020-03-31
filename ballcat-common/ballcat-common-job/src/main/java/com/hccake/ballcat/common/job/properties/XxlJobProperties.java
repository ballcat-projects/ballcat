package com.hccake.ballcat.common.job.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lengleng
 * @date 2019-09-18
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {
	private XxlAdminProperties admin = new XxlAdminProperties();

	private XxlExecutorProperties executor = new XxlExecutorProperties();
}