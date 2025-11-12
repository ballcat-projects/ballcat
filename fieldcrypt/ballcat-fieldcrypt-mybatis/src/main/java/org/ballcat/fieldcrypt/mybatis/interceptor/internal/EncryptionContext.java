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

package org.ballcat.fieldcrypt.mybatis.interceptor.internal;

import java.util.Set;

import lombok.Getter;

/**
 * 单次拦截的加密上下文，包含回滚记录器和运行期控制参数。
 */
@Getter
public final class EncryptionContext {

	private final MutationRecorder recorder;

	private final boolean failFast;

	private final Set<Object> processed;

	public EncryptionContext(MutationRecorder recorder, boolean failFast, Set<Object> processed) {
		this.recorder = recorder;
		this.failFast = failFast;
		this.processed = processed;
	}

	public boolean isProcessed(Object o) {
		return this.processed.contains(o);
	}

	public void markProcessed(Object o) {
		this.processed.add(o);
	}

}
