package com.hccake.ballcat.common.job.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author lengleng
 * @date 2019-09-18
 */
@Data
@ConfigurationProperties(prefix = XxlJobProperties.PREFIX)
public class XxlJobProperties {

	public static final String PREFIX = "ballcat.xxl.job";

	/**
	 * 是否启用分布式调度任务，默认：开启
	 */
	private boolean enabled = true;

	/**
	 * 执行器通讯TOKEN [选填]：非空时启用；
	 */
	private String accessToken;

	@NestedConfigurationProperty
	private XxlAdminProperties admin = new XxlAdminProperties();

	@NestedConfigurationProperty
	private XxlExecutorProperties executor = new XxlExecutorProperties();

}