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

package org.ballcat.fieldcrypt.mybatisplus;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.ballcat.fieldcrypt.core.cache.ClassMetaResolver;
import org.ballcat.fieldcrypt.core.runtime.FieldCryptRuntimeConfig;
import org.ballcat.fieldcrypt.crypto.CryptoAlgorithmRegistry;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;
import org.ballcat.fieldcrypt.crypto.DefaultCryptoEngine;
import org.ballcat.fieldcrypt.crypto.impl.AesCbcFixedIvCryptoAlgorithm;
import org.ballcat.fieldcrypt.mybatis.interceptor.MybatisParameterEncryptInterceptor;
import org.ballcat.fieldcrypt.mybatis.interceptor.MybatisResultDecryptInterceptor;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisMethodMetadataCache;
import org.ballcat.fieldcrypt.mybatisplus.weave.MpWeaveRuntime;
import org.ballcat.fieldcrypt.util.AESKeyGenerator;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

	@Bean
	public DataSource dataSource() {
		org.apache.ibatis.datasource.pooled.PooledDataSource ds = new org.apache.ibatis.datasource.pooled.PooledDataSource(
				"org.h2.Driver", "jdbc:h2:mem:mp-ds;MODE=MYSQL;DB_CLOSE_DELAY=-1", "sa", "");
		// init schema
		try (Connection c = ds.getConnection(); Statement st = c.createStatement()) {
			st.execute("CREATE TABLE IF NOT EXISTS t_user (\n" + "  id BIGINT PRIMARY KEY,\n" + "  name VARCHAR(64),\n"
					+ "  mobile VARCHAR(256),\n" + "  email VARCHAR(256),\n" + "  address VARCHAR(256)\n" + ")");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		return ds;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource, MybatisParameterEncryptInterceptor pEncrypt,
			MybatisResultDecryptInterceptor rDecrypt) throws Exception {
		// Use MP's factory bean so BaseMapper works out-of-the-box
		MybatisSqlSessionFactoryBean fb = new MybatisSqlSessionFactoryBean();
		fb.setDataSource(dataSource);
		MybatisConfiguration cfg = new MybatisConfiguration();
		cfg.setMapUnderscoreToCamelCase(true);
		fb.setConfiguration(cfg);
		fb.setTypeAliasesPackage("org.ballcat.fieldcrypt.mybatisplus.model");
		fb.setPlugins(new Interceptor[] { pEncrypt, rDecrypt });
		return fb.getObject();
	}

	@Bean
	public ClassMetaResolver classMetaResolver() {
		return new ClassMetaResolver();
	}

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

	@Bean
	public CryptoAlgorithmRegistry cryptoAlgorithmRegistry() {
		CryptoAlgorithmRegistry reg = new CryptoAlgorithmRegistry();
		reg.register(new AesCbcFixedIvCryptoAlgorithm(AESKeyGenerator.generateAES256Key()));
		return reg;
	}

	@Bean
	public CryptoEngine cryptoEngine(CryptoAlgorithmRegistry registry) {
		DefaultCryptoEngine engine = new DefaultCryptoEngine(registry);
		engine.setDefaultAlgo(AesCbcFixedIvCryptoAlgorithm.ALGO_NAME);
		return engine;
	}

	@Bean
	public FieldCryptRuntimeConfig fieldCryptRuntimeConfig() {
		FieldCryptRuntimeConfig cfg = new FieldCryptRuntimeConfig();
		cfg.replace(new FieldCryptRuntimeConfig.Snapshot(true, // enabled
				true, // enableResult (不影响 MP 测试)
				true, // enableParameter
				true, // failFast
				false, // restorePlaintext
				AesCbcFixedIvCryptoAlgorithm.ALGO_NAME));
		return cfg;
	}

	@Bean
	public SmartInitializingSingleton mpWeaverBootstrap(CryptoEngine crypto, ClassMetaResolver resolver,
			FieldCryptRuntimeConfig runtime) {
		return () -> MpWeaveRuntime.init(crypto, resolver, runtime);
	}

}
