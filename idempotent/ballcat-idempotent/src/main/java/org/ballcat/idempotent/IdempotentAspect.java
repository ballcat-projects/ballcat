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

package org.ballcat.idempotent;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.ballcat.common.model.result.BaseResultCode;
import org.ballcat.common.util.Assert;
import org.ballcat.idempotent.annotation.Idempotent;
import org.ballcat.idempotent.exception.IdempotentException;
import org.ballcat.idempotent.key.generator.IdempotentKeyGenerator;
import org.ballcat.idempotent.key.store.IdempotentKeyStore;

/**
 * @author hccake
 */
@Aspect
@RequiredArgsConstructor
public class IdempotentAspect {

	private final IdempotentKeyStore idempotentKeyStore;

	private final IdempotentKeyGenerator idempotentKeyGenerator;

	@Around("@annotation(idempotentAnnotation)")
	public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotentAnnotation) throws Throwable {
		// 获取幂等标识
		String idempotentKey = this.idempotentKeyGenerator.generate(joinPoint, idempotentAnnotation);

		// 校验当前请求是否重复请求
		boolean saveSuccess = this.idempotentKeyStore.saveIfAbsent(idempotentKey, idempotentAnnotation.duration(),
				idempotentAnnotation.timeUnit());
		Assert.isTrue(saveSuccess, () -> {
			throw new IdempotentException(BaseResultCode.REPEATED_EXECUTE.getCode(), idempotentAnnotation.message());
		});

		try {
			Object result = joinPoint.proceed();
			if (idempotentAnnotation.removeKeyWhenFinished()) {
				this.idempotentKeyStore.remove(idempotentKey);
			}
			return result;
		}
		catch (Throwable e) {
			// 异常时，根据配置决定是否删除幂等 key
			if (idempotentAnnotation.removeKeyWhenError()) {
				this.idempotentKeyStore.remove(idempotentKey);
			}
			throw e;
		}

	}

}
