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

package org.ballcat.fieldcrypt.mybatisplus.weave;

import org.ballcat.fieldcrypt.core.cache.ClassMetaResolver;
import org.ballcat.fieldcrypt.core.runtime.FieldCryptRuntimeConfig;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;

/**
 * 运行时服务持有者，用于在 ByteBuddy 增强代码中访问加密运行时服务。 提供对加密引擎、类元数据解析器和字段加密配置的静态访问能力。
 *
 * @author Hccake
 * @since 2.0.0
 */
public final class MpWeaveRuntime {

	private static volatile CryptoEngine crypto;

	private static volatile ClassMetaResolver resolver;

	private static volatile FieldCryptRuntimeConfig runtime;

	private MpWeaveRuntime() {
	}

	public static void init(CryptoEngine c, ClassMetaResolver r, FieldCryptRuntimeConfig cfg) {
		crypto = c;
		resolver = r;
		runtime = cfg;
	}

	public static CryptoEngine crypto() {
		return crypto;
	}

	public static ClassMetaResolver resolver() {
		return resolver;
	}

	public static FieldCryptRuntimeConfig.Snapshot snap() {
		return runtime != null ? runtime.get() : null;
	}

}
