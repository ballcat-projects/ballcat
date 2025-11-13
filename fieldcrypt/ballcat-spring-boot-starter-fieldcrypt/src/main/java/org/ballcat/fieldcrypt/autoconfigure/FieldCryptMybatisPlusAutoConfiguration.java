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
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-config to install ByteBuddy weaver for MyBatis-Plus Wrapper.
 *
 * @author Hccake
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass(name = "com.baomidou.mybatisplus.core.conditions.AbstractWrapper")
@AutoConfigureAfter(name = "org.ballcat.fieldcrypt.autoconfigure.FieldCryptAutoConfiguration")
@AutoConfigureBefore(name = "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration")
public class FieldCryptMybatisPlusAutoConfiguration {

	@Bean
	public MpWeaverBootstrap mpWrapperEncryptWeaverStarter(CryptoEngine crypto, ClassMetaResolver resolver,
			FieldCryptRuntimeConfig runtime) {
		return new MpWeaverBootstrap(crypto, resolver, runtime);
	}

}
