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

import org.apache.ibatis.plugin.Interceptor;
import org.ballcat.fieldcrypt.core.cache.ClassMetaResolver;
import org.ballcat.fieldcrypt.core.runtime.FieldCryptRuntimeConfig;
import org.ballcat.fieldcrypt.crypto.CryptoAlgorithmRegistry;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;
import org.ballcat.fieldcrypt.crypto.DefaultCryptoEngine;
import org.ballcat.fieldcrypt.crypto.impl.AesCbcFixedIvCryptoAlgorithm;
import org.ballcat.fieldcrypt.crypto.spi.CryptoAlgorithm;
import org.ballcat.fieldcrypt.mybatis.interceptor.MybatisParameterEncryptInterceptor;
import org.ballcat.fieldcrypt.mybatis.interceptor.MybatisResultDecryptInterceptor;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisMethodMetadataCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * FieldCrypt 自动配置
 * <p>
 * 条件: 1. 存在 MyBatis Interceptor 类 2. 开启配置 ballcat.fieldcrypt.enabled (默认开启)
 *
 * @author Hccake
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass(Interceptor.class)
@EnableConfigurationProperties(FieldCryptProperties.class)
public class FieldCryptAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ClassMetaResolver classMetaResolver() {
		return new ClassMetaResolver();
	}

	/**
	 * 注册内置算法作为默认 Bean（仅当用户未提供任何 CryptoAlgorithm Bean 时生效）。
	 */
	@Bean
	@ConditionalOnMissingBean(CryptoAlgorithm.class)
	public CryptoAlgorithm defaultCryptoAlgorithm(FieldCryptProperties props) {
		String aesKey = props.getAesKey();
		Assert.isTrue(StringUtils.hasText(aesKey), "AES Key cannot be empty!");
		return new AesCbcFixedIvCryptoAlgorithm(aesKey);
	}

	@Bean
	@ConditionalOnMissingBean
	public CryptoAlgorithmRegistry cryptoAlgorithmRegistry(java.util.List<CryptoAlgorithm> algorithms) {
		CryptoAlgorithmRegistry registry = new CryptoAlgorithmRegistry();
		if (algorithms != null) {
			for (CryptoAlgorithm algo : algorithms) {
				registry.register(algo);
			}
		}
		return registry;
	}

	@Bean
	@ConditionalOnMissingBean
	public FieldCryptRuntimeConfig fieldCryptRuntimeConfig(FieldCryptProperties props) {
		FieldCryptRuntimeConfig cfg = new FieldCryptRuntimeConfig();
		cfg.replace(new FieldCryptRuntimeConfig.Snapshot(props.isEnabled(), props.isEnableParameter(),
				props.isEnableResult(), props.isFailFast(), props.isRestorePlaintext(), props.getDefaultAlgo()));
		return cfg;
	}

	@Bean
	@ConditionalOnMissingBean(CryptoEngine.class)
	public CryptoEngine cryptoEngine(FieldCryptProperties props, CryptoAlgorithmRegistry algorithms) {
		DefaultCryptoEngine engine = new DefaultCryptoEngine(algorithms);
		engine.setDefaultAlgo(props.getDefaultAlgo());
		return engine;
	}

	/**
	 * Mybatis 参数加密拦截器，默认优先级最低，保证可以最先执行。
	 * @param crypto 加密引擎
	 * @param resolver 解析器
	 * @param runtime 运行时配置
	 * @param methodCache 方法元数据缓存
	 * @return MybatisParameterEncryptInterceptor
	 */
	@Order
	@Bean
	@ConditionalOnMissingBean
	public MybatisParameterEncryptInterceptor parameterEncryptInterceptor(CryptoEngine crypto,
			ClassMetaResolver resolver, FieldCryptRuntimeConfig runtime, MybatisMethodMetadataCache methodCache) {
		return new MybatisParameterEncryptInterceptor(crypto, resolver, runtime, methodCache);
	}

	@Bean
	@ConditionalOnMissingBean
	public MybatisResultDecryptInterceptor resultDecryptInterceptor(CryptoEngine crypto, ClassMetaResolver resolver,
			FieldCryptRuntimeConfig runtime, MybatisMethodMetadataCache methodCache) {
		return new MybatisResultDecryptInterceptor(crypto, resolver, runtime, methodCache);
	}

	@Bean
	@ConditionalOnMissingBean
	public MybatisMethodMetadataCache mybatisMethodMetadataCache() {
		return new MybatisMethodMetadataCache();
	}

}
