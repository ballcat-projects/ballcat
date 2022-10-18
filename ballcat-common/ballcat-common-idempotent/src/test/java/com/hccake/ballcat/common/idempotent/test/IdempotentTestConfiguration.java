package com.hccake.ballcat.common.idempotent.test;

import com.hccake.ballcat.common.idempotent.IdempotentAspect;
import com.hccake.ballcat.common.idempotent.key.IdempotentKeyStore;
import com.hccake.ballcat.common.idempotent.key.KeyPrefixGenerator;
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
	public KeyPrefixGenerator keyPrefixGenerator() {
		return () -> "";
	}

	@Bean
	public IdempotentAspect idempotentAspect(IdempotentKeyStore idempotentKeyStore) {
		return new IdempotentAspect(idempotentKeyStore, keyPrefixGenerator());
	}

	@Bean
	public IdempotentMethods idempotentMethods() {
		return new IdempotentMethods();
	}

}
