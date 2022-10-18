package com.hccake.ballcat.autoconfigure.idempotent;

import com.hccake.ballcat.common.idempotent.IdempotentAspect;
import com.hccake.ballcat.common.idempotent.key.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 幂等自动装配
 *
 * @author lishangbu
 * @date 2022/10/18
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(IdempotentProperties.class)
public class IdempotentAutoConfiguration {

	/**
	 * 提供默认幂等前缀生成器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public KeyGenerator keyGenerator() {
		return new DefaultKeyGenerator();
	}

	@Bean
	@ConditionalOnMissingBean
	public IdempotentKeyStore idempotentKeyStore(IdempotentProperties properties) {
		IdempotentProperties.KeyStoreType keyStoreType = properties.getKeyStoreType();
		if (keyStoreType.equals(IdempotentProperties.KeyStoreType.REDIS)) {
			return new RedisIdempotentKeyStore();
		}
		else {
			return new InMemoryIdempotentKeyStore();
		}
	}

	@Bean
	public IdempotentAspect idempotentAspect(IdempotentKeyStore idempotentKeyStore) {
		return new IdempotentAspect(idempotentKeyStore, keyGenerator());
	}

}
