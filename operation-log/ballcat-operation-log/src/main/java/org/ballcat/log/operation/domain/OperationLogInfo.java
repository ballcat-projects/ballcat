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

package org.ballcat.log.operation.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;
import org.ballcat.log.operation.enums.OperationStatusEnum;

/**
 * 操作日志信息.
 *
 * @author Hccake
 * @since 2.0.0
 */
@Accessors(chain = true)
@Data
public class OperationLogInfo {

	/**
	 * 业务标识。
	 */
	private String bizNo;

	/**
	 * 业务类型。
	 */
	private String bizType;

	/**
	 * 业务子类型。
	 */
	private String subType;

	/**
	 * 操作人。
	 */
	private String operator;

	/**
	 * 操作消息。
	 */
	private String message;

	/**
	 * 执行方法的所属的全限定类名。
	 */
	private String className;

	/**
	 * 执行方法的方法名。
	 */
	private String methodName;

	/**
	 * 执行方法的入参。
	 */
	private String methodArgs;

	/**
	 * 执行方法的返回值。
	 */
	private String methodResult;

	/**
	 * 错误堆栈。
	 */
	private String errorStack;

	/**
	 * 额外信息。
	 */
	private String extra;

	/**
	 * 是否成功。
	 */
	private String traceId;

	/**
	 * 操作状态。 1：成功 0：失败 -1：执行异常
	 *
	 * @see OperationStatusEnum
	 */
	private Integer status;

	/**
	 * HTTP信息。
	 */
	private HttpInfo httpInfo;

	/**
	 * 执行时间。
	 */
	private Long executionTime;

	/**
	 * 操作时间。
	 */
	private LocalDateTime operationTime;

	@Data
	public static class HttpInfo {

		/**
		 * 请求地址。
		 */
		private String requestUri;

		/**
		 * 请求方式。
		 */
		private String requestMethod;

		/**
		 * 客户端IP。
		 */
		private String clientIp;

		/**
		 * 用户代理。
		 */
		private String userAgent;

		/**
		 * 请求来源。
		 */
		private String referer;

	}

}
