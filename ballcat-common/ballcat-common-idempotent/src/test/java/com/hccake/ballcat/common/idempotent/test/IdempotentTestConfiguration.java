package com.hccake.ballcat.common.idempotent.test;

import com.hccake.ballcat.common.idempotent.IdempotentAspect;
import com.hccake.ballcat.common.idempotent.key.DefaultKeyGenerator;
import com.hccake.ballcat.common.idempotent.key.IdempotentKeyStore;
import com.hccake.ballcat.common.idempotent.key.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author hccake
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan
public class IdempotentTestConfiguration {

	@Bean
	public KeyGenerator keyGenerator() {
		return new DefaultKeyGenerator();
	}

	@Bean
	public IdempotentAspect idempotentAspect(IdempotentKeyStore idempotentKeyStore) {
		return new IdempotentAspect(idempotentKeyStore, keyGenerator());
	}

	@Bean
	public IdempotentMethods idempotentMethods() {
		return new IdempotentMethods();
	}

}
