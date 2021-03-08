package com.hccake.ballcat.common.conf.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lingting 2020-10-13 22:39
 */
@Data
@ConfigurationProperties(prefix = XssProperties.PREFIX)
public class XssProperties {

	public static final String PREFIX = "ballcat.security.xss";

	/**
	 * 是否开启
	 */
	private Boolean enabled = true;

	/**
	 * xss 过滤包含的路径（Ant风格）
	 **/
	private Set<String> includePaths = Collections.singleton("/**");

	/**
	 * xss 需要排除的路径（Ant风格），优先级高于包含路径
	 **/
	private Set<String> excludePaths = new HashSet<>();

	/**
	 * 需要处理的 HTTP 请求方法集合
	 */
	private final Set<String> includeHttpMethods = new HashSet<>(
			Arrays.asList(HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.PATCH.name()));

}
