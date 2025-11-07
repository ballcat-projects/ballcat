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

package org.ballcat.fieldcrypt.core.runtime;

import java.util.concurrent.atomic.AtomicReference;

import lombok.ToString;

/**
 * 运行时配置快照持有者（无 Spring 依赖）。 拦截器按次读取 Snapshot，刷新时整包替换，读路径无锁。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class FieldCryptRuntimeConfig {

	private final AtomicReference<Snapshot> ref = new AtomicReference<>(
			new Snapshot(true, true, true, true, false, null));

	public Snapshot get() {
		return this.ref.get();
	}

	public void replace(Snapshot snapshot) {
		if (snapshot == null) {
			throw new IllegalArgumentException("snapshot must not be null");
		}
		this.ref.set(snapshot);
	}

	@ToString
	public static final class Snapshot {

		public final boolean enabled;

		public final boolean enableParameter;

		public final boolean enableResult;

		public final boolean failFast;

		public final boolean restorePlaintext;

		public final String defaultAlgo;

		public Snapshot(boolean enabled, boolean enableParameter, boolean enableResult, boolean failFast,
				boolean restorePlaintext, String defaultAlgo) {
			this.enabled = enabled;
			this.enableParameter = enableParameter;
			this.enableResult = enableResult;
			this.failFast = failFast;
			this.restorePlaintext = restorePlaintext;
			this.defaultAlgo = defaultAlgo == null ? null : defaultAlgo.trim();
		}

	}

}
