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

package org.ballcat.fieldcrypt.autoconfigure;

import org.ballcat.fieldcrypt.core.cache.ClassMetaResolver;
import org.ballcat.fieldcrypt.core.runtime.FieldCryptRuntimeConfig;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;
import org.ballcat.fieldcrypt.mybatisplus.weave.MpWeaveRuntime;
import org.springframework.beans.factory.SmartInitializingSingleton;

/**
 *
 * 引导类 Bean，用于在所有单例 Bean 创建完成后， 使用 Spring 管理的依赖项初始化 MpWeaveRuntime。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class MpWeaverBootstrap implements SmartInitializingSingleton {

	private final CryptoEngine crypto;

	private final ClassMetaResolver resolver;

	private final FieldCryptRuntimeConfig runtime;

	public MpWeaverBootstrap(CryptoEngine crypto, ClassMetaResolver resolver, FieldCryptRuntimeConfig runtime) {
		this.crypto = crypto;
		this.resolver = resolver;
		this.runtime = runtime;
	}

	@Override
	public void afterSingletonsInstantiated() {
		MpWeaveRuntime.init(this.crypto, this.resolver, this.runtime);
	}

}
