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

package org.ballcat.log.operation.handler;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.log.operation.domain.OperationLogInfo;

/**
 * 默认的操作日志处理器，将操作日志信息打印到日志中。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Slf4j
public class DefaultOperationLogHandler implements OperationLogHandler {

	@Override
	public void handle(OperationLogInfo operationLogInfo) {
		log.info("Operation Log: {}", JsonUtils.toJson(operationLogInfo));
	}

}
