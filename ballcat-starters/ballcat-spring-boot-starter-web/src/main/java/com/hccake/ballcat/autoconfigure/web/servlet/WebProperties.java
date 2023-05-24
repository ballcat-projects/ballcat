package com.hccake.ballcat.autoconfigure.web.servlet;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hccake
 */
@Data
@ConfigurationProperties(prefix = "ballcat.web")
public class WebProperties {

	/**
	 * traceId 的 http 头名称
	 */
	private String traceIdHeaderName = "X-Trace-Id";

}
