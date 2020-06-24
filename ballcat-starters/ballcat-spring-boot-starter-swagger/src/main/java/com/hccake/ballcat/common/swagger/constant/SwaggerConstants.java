package com.hccake.ballcat.common.swagger.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/11/1 20:03
 */
public final class SwaggerConstants {

	private SwaggerConstants() {
	}

	/**
	 * 默认的排除路径，排除Spring Boot默认的错误处理路径和端点
	 */
	public static final List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/error", "/actuator/**");

	/**
	 * 默认扫描路径
	 */
	public static final String BASE_PATH = "/**";

	/**
	 * 默认的文档提供路径
	 */
	public static final String API_URI = "/v2/api-docs";

}
