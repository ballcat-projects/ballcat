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
package com.hccake.starter.grpc.log;

import com.hccake.starter.grpc.enums.GrpcLogType;
import lombok.Data;

/**
 * @author lingting 2023-04-13 14:36
 */
@Data
public class GrpcLog {

	private String traceId;

	private GrpcLogType type;

	/**
	 * 服务名
	 */
	private String service;

	/**
	 * 方法名
	 */
	private String method;

	/**
	 * host
	 */
	private String host;

	/**
	 * port
	 */
	private String port;

	/**
	 * 耗时, 单位: 毫秒
	 */
	private Long time;

}
