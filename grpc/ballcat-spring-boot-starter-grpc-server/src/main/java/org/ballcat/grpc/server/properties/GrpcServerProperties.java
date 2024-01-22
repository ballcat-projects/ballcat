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

package org.ballcat.grpc.server.properties;

import java.util.concurrent.TimeUnit;

import lombok.Data;
import org.ballcat.common.core.constant.MDCConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

/**
 * @author lingting 2023-04-06 15:18
 */
@Data
@ConfigurationProperties(GrpcServerProperties.PREFIX)
public class GrpcServerProperties {

	public static final String PREFIX = "ballcat.grpc.server";

	private Integer port;

	private String traceIdKey = MDCConstants.TRACE_ID_KEY;

	private DataSize messageSize = DataSize.ofMegabytes(512);

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTime = TimeUnit.HOURS.toMillis(2);

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTimeout = TimeUnit.SECONDS.toMillis(20);

}
