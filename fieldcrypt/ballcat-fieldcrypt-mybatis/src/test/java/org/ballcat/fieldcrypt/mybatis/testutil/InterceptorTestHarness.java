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

package org.ballcat.fieldcrypt.mybatis.testutil;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.ballcat.fieldcrypt.core.cache.ClassMetaResolver;
import org.ballcat.fieldcrypt.core.runtime.FieldCryptRuntimeConfig;
import org.ballcat.fieldcrypt.crypto.CryptoAlgorithmRegistry;
import org.ballcat.fieldcrypt.crypto.CryptoContext;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;
import org.ballcat.fieldcrypt.crypto.DefaultCryptoEngine;
import org.ballcat.fieldcrypt.crypto.impl.AesCbcFixedIvCryptoAlgorithm;
import org.ballcat.fieldcrypt.mybatis.interceptor.MybatisParameterEncryptInterceptor;
import org.ballcat.fieldcrypt.mybatis.interceptor.MybatisResultDecryptInterceptor;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisMethodMetadataCache;
import org.ballcat.fieldcrypt.mybatis.repository.UserJdbcRepository;
import org.ballcat.fieldcrypt.util.AESKeyGenerator;

/**
 * 集成测试统一引导器：管理数据源、工厂构建、助手方法。
 *
 * @author Hccake
 * @since 2.0.0
 */
public final class InterceptorTestHarness {

	private static final AesCbcFixedIvCryptoAlgorithm FIXED_ALGO = new AesCbcFixedIvCryptoAlgorithm(
			AESKeyGenerator.generateAES256Key());

	private final DataSource dataSource;

	private final UserJdbcRepository userRepo;

	private final CryptoEngine testEngine;

	private final Map<TestConfig, SqlSessionFactory> factories = new ConcurrentHashMap<>();

	public InterceptorTestHarness() {
		this.dataSource = new PooledDataSource("org.h2.Driver", "jdbc:h2:mem:ds;MODE=MYSQL;DB_CLOSE_DELAY=-1", "sa",
				"");
		this.userRepo = new UserJdbcRepository(this.dataSource);
		// 构建与实际 MyBatis 拦截器一致的引擎（带 ENC: 前缀语义）用于计算测试期望值
		CryptoAlgorithmRegistry reg = new CryptoAlgorithmRegistry();
		reg.register(FIXED_ALGO);
		DefaultCryptoEngine engine = new DefaultCryptoEngine(reg);
		engine.setDefaultAlgo(AesCbcFixedIvCryptoAlgorithm.ALGO_NAME);
		this.testEngine = engine;
		initSchema();
	}

	private void initSchema() {
		try (Connection c = this.dataSource.getConnection(); Statement st = c.createStatement()) {
			st.execute(
					"CREATE TABLE IF NOT EXISTS t_user (id BIGINT PRIMARY KEY, name VARCHAR(64), mobile VARCHAR(256), email VARCHAR(256), address VARCHAR(256))");
			st.execute("TRUNCATE TABLE t_user");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public DataSource dataSource() {
		return this.dataSource;
	}

	public UserJdbcRepository userRepo() {
		return this.userRepo;
	}

	public String encryptDeterministic(String plain) {
		return this.testEngine.encrypt(plain, new CryptoContext(AesCbcFixedIvCryptoAlgorithm.ALGO_NAME, ""));
	}

	public void truncate() {
		try (Connection c = this.dataSource.getConnection(); Statement st = c.createStatement()) {
			st.execute("TRUNCATE TABLE t_user");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public SqlSessionFactory factory(TestConfig cfg) {
		return this.factories.computeIfAbsent(cfg, this::buildFactory);
	}

	private SqlSessionFactory buildFactory(TestConfig cfg) {
		String defaultAlgo = AesCbcFixedIvCryptoAlgorithm.ALGO_NAME;
		CryptoAlgorithmRegistry reg = new CryptoAlgorithmRegistry();
		reg.register(FIXED_ALGO);
		CryptoEngine crypto = new DefaultCryptoEngine(reg);
		crypto.setDefaultAlgo(defaultAlgo);

		ClassMetaResolver resolver = new ClassMetaResolver();
		FieldCryptRuntimeConfig runtime = new FieldCryptRuntimeConfig();
		runtime.replace(new FieldCryptRuntimeConfig.Snapshot(true, true, true, cfg.failFast, cfg.restorePlaintext,
				defaultAlgo));
		MybatisMethodMetadataCache cache = new MybatisMethodMetadataCache();
		MybatisParameterEncryptInterceptor pencrypt = new MybatisParameterEncryptInterceptor(crypto, resolver, runtime,
				cache);
		MybatisResultDecryptInterceptor pdecrypt = new MybatisResultDecryptInterceptor(crypto, resolver, runtime,
				cache);

		org.apache.ibatis.session.Configuration ibCfg = new org.apache.ibatis.session.Configuration();
		ibCfg.setUseActualParamName(cfg.useActualParamName);
		ibCfg.setLazyLoadingEnabled(cfg.lazyLoadingEnabled);
		ibCfg.setAggressiveLazyLoading(false);
		ibCfg.addMapper(org.ballcat.fieldcrypt.mybatis.mapper.UserMapper.class);
		ibCfg.addInterceptor(pdecrypt);
		ibCfg.addInterceptor(pencrypt);
		// 先注册“解密”，最后注册“加密”的顺序，后注册“分页风格拦截器”，以便测试 6 参数链路
		if (cfg.usePagingLikeInterceptor) {
			ibCfg.addInterceptor(new PagingLikeInterceptor());
		}
		ibCfg.setEnvironment(new Environment("test-" + cfg.hashCode(), new JdbcTransactionFactory(), this.dataSource));
		return new SqlSessionFactoryBuilder().build(ibCfg);
	}

	public <M, R> R withMapper(SqlSessionFactory sf, Class<M> mapperType, Function<M, R> fn) {
		try (SqlSession session = sf.openSession(true)) {
			M mapper = session.getMapper(mapperType);
			return fn.apply(mapper);
		}
	}

	public <M> void withMapper(SqlSessionFactory sf, Class<M> mapperType, Consumer<M> fn) {
		withMapper(sf, mapperType, m -> {
			fn.accept(m);
			return null;
		});
	}

}
