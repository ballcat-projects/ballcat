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

package org.ballcat.log.operation.provider;

import org.ballcat.common.core.constant.MDCConstants;
import org.slf4j.MDC;

/**
 * TraceId 提供者。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class DefaultTraceIdProvider implements TraceIdProvider {

	@Override
	public String get() {
		return MDC.get(MDCConstants.TRACE_ID_KEY);
	}

}
