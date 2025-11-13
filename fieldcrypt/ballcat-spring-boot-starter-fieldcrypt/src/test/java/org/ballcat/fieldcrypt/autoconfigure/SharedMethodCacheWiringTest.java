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

import java.lang.reflect.Field;

import org.ballcat.fieldcrypt.mybatis.interceptor.MybatisParameterEncryptInterceptor;
import org.ballcat.fieldcrypt.mybatis.interceptor.MybatisResultDecryptInterceptor;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisMethodMetadataCache;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verify both interceptors share the same MybatisMethodMetadataCache bean.
 */
public class SharedMethodCacheWiringTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(FieldCryptAutoConfiguration.class))
		.withPropertyValues("ballcat.fieldcrypt.enabled=true", "ballcat.fieldcrypt.enable-parameter=true",
				"ballcat.fieldcrypt.enable-result=true", "ballcat.fieldcrypt.aes-key=6DvlLtb5S7c49usgi8AaWFlsehzH4QUe");

	@Test
	void interceptorsShareSameCacheBean() {
		this.contextRunner.run(ctx -> {
			MybatisParameterEncryptInterceptor pei = ctx.getBean(MybatisParameterEncryptInterceptor.class);
			MybatisResultDecryptInterceptor rdi = ctx.getBean(MybatisResultDecryptInterceptor.class);
			MybatisMethodMetadataCache cacheBean = ctx.getBean(MybatisMethodMetadataCache.class);

			// reflect private field to compare instance identity
			MybatisMethodMetadataCache c1 = (MybatisMethodMetadataCache) getPrivate(pei, "mybatisMethodMetadataCache");
			MybatisMethodMetadataCache c2 = (MybatisMethodMetadataCache) getPrivate(rdi, "mybatisMethodMetadataCache");
			assertThat(c1).isSameAs(cacheBean);
			assertThat(c2).isSameAs(cacheBean);
		});
	}

	private Object getPrivate(Object target, String field) throws Exception {
		Field f = target.getClass().getDeclaredField(field);
		f.setAccessible(true);
		return f.get(target);
	}

}
