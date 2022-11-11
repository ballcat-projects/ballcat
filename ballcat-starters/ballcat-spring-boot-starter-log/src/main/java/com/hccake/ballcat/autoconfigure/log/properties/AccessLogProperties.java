package com.hccake.ballcat.autoconfigure.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * 访问日志配置
 *
 * @author Hccake 2020/6/11 14:56
 * @version 1.0
 */
@Data
@ConfigurationProperties(prefix = AccessLogProperties.PREFIX)
public class AccessLogProperties {

	public static final String PREFIX = "ballcat.log.access";

	/**
	 * 开启 access log 的记录
	 */
	private boolean enabled = true;

	/**
	 * 忽略的Url匹配规则，Ant风格
	 */
	private List<String> ignoreUrlPatterns = Arrays.asList("/actuator/**", "/webjars/**", "/favicon.ico",
			"/swagger-ui/**", "/bycdao-ui/**", "/captcha/get");

}