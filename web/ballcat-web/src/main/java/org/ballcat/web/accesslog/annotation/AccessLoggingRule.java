package org.ballcat.web.accesslog.annotation;

import java.lang.annotation.*;

/**
 * @author Alickx
 * @date 2023/11/23 17:48
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLoggingRule {

	/**
	 * 忽略记录
	 */
	boolean ignore() default false;

	/**
	 * 记录查询参数
	 */
	boolean includeQueryString() default false;

	/**
	 * 记录请求体
	 */
	boolean includeRequestBody() default false;

	/**
	 * 记录响应体
	 */
	boolean includeResponseBody() default false;

}
