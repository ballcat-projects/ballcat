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
import org.ballcat.fieldcrypt.crypto.impl.AesCbcFixedIvCryptoAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldCryptAutoConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(FieldCryptAutoConfiguration.class))
		.withPropertyValues("ballcat.fieldcrypt.enabled=true", "ballcat.fieldcrypt.enable-parameter=true",
				"ballcat.fieldcrypt.enable-result=true", "ballcat.fieldcrypt.aes-key=6DvlLtb5S7c49usgi8AaWFlsehzH4QUe");

	@Test
	void createsBeansByDefault() {
		this.contextRunner.run(ctx -> {
			assertThat(ctx).hasSingleBean(ClassMetaResolver.class);
			assertThat(ctx).hasSingleBean(CryptoEngine.class);
			assertThat(ctx).hasBean("parameterEncryptInterceptor");
			assertThat(ctx).hasBean("resultDecryptInterceptor");
		});
	}

	@Test
	void disablesAllByProperty() {
		new ApplicationContextRunner().withConfiguration(AutoConfigurations.of(FieldCryptAutoConfiguration.class))
			.withPropertyValues("ballcat.fieldcrypt.enabled=false")
			.withPropertyValues("ballcat.fieldcrypt.aes-key=6DvlLtb5S7c49usgi8AaWFlsehzH4QUe")
			.run(ctx -> {
				// Beans are still created; runtime flag controls global enable switch
				assertThat(ctx).hasSingleBean(ClassMetaResolver.class);
				assertThat(ctx).hasBean("parameterEncryptInterceptor");
				assertThat(ctx).hasBean("resultDecryptInterceptor");
				FieldCryptRuntimeConfig runtime = ctx.getBean(FieldCryptRuntimeConfig.class);
				assertThat(runtime.get().enabled).isFalse();
			});
	}

	@Test
	void disablesParameterByProperty() {
		this.contextRunner.withPropertyValues("ballcat.fieldcrypt.enable-parameter=false").run(ctx -> {
			// Interceptor beans are still created; runtime flag controls behavior
			assertThat(ctx).hasBean("parameterEncryptInterceptor");
			FieldCryptRuntimeConfig runtime = ctx.getBean(FieldCryptRuntimeConfig.class);
			assertThat(runtime.get().enableParameter).isFalse();
			// Other interceptor remains enabled by flag
			assertThat(runtime.get().enableResult).isTrue();
		});
	}

	@Test
	void disablesResultByProperty() {
		this.contextRunner.withPropertyValues("ballcat.fieldcrypt.enable-result=false").run(ctx -> {
			// Interceptor beans are still created; runtime flag controls behavior
			assertThat(ctx).hasBean("resultDecryptInterceptor");
			FieldCryptRuntimeConfig runtime = ctx.getBean(FieldCryptRuntimeConfig.class);
			assertThat(runtime.get().enableResult).isFalse();
			// Other interceptor remains enabled by flag
			assertThat(runtime.get().enableParameter).isTrue();
		});
	}

	@Test
	void registersDefaultAlgorithmWhenMissing() {
		this.contextRunner.run(ctx -> {
			CryptoEngine reg = ctx.getBean(CryptoEngine.class);
			// 应该已注册默认算法（兜底）
			String cipher = reg.encrypt("abc",
					new org.ballcat.fieldcrypt.crypto.CryptoContext(AesCbcFixedIvCryptoAlgorithm.ALGO_NAME, ""));
			String plain = reg.decrypt(cipher,
					new org.ballcat.fieldcrypt.crypto.CryptoContext(AesCbcFixedIvCryptoAlgorithm.ALGO_NAME, ""));
			assertThat(plain).isEqualTo("abc");
		});
	}

	@Test
	void backsOffWhenInterceptorNotOnClasspath() {
		new ApplicationContextRunner().withConfiguration(AutoConfigurations.of(FieldCryptAutoConfiguration.class))
			.withClassLoader(new FilteredClassLoader("org.apache.ibatis.plugin.Interceptor"))
			.run(ctx -> assertThat(ctx).doesNotHaveBean(CryptoEngine.class));
	}

}
