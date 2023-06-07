/*
 * Copyright 2023 the original author or authors.
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
package org.ballcat.log.access.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 访问日志处理器
 *
 * @author Hccake 2019/10/15 22:21
 */
public interface AccessLogHandler<T> {

	/**
	 * 记录日志
	 * @param request 请求信息
	 * @param response 响应信息
	 * @param executionTime 执行时长
	 * @param throwable 异常
	 */
	default void handleLog(HttpServletRequest request, HttpServletResponse response, Long executionTime,
			Throwable throwable) {
		T log = buildLog(request, response, executionTime, throwable);
		saveLog(log);
	}

	/**
	 * 生产一个日志
	 * @return accessLog
	 * @param request 请求信息
	 * @param response 响应信息
	 * @param executionTime 执行时长
	 * @param throwable 异常
	 */
	T buildLog(HttpServletRequest request, HttpServletResponse response, Long executionTime, Throwable throwable);

	/**
	 * 保存日志 落库/或输出到文件等
	 * @param accessLog 访问日志
	 */
	void saveLog(T accessLog);

}
