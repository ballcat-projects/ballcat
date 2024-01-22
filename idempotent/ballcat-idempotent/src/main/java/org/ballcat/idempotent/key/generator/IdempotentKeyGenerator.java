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

package org.ballcat.idempotent.key.generator;

import org.aspectj.lang.JoinPoint;
import org.ballcat.idempotent.annotation.Idempotent;
import org.springframework.lang.NonNull;

/**
 * 幂等key生成器
 *
 * @author lishangbu 2022/10/18
 */
@FunctionalInterface
public interface IdempotentKeyGenerator {

	/**
	 * 生成幂等 key
	 * @param joinPoint 切点
	 * @param idempotentAnnotation 幂等注解
	 * @return 幂等key标识
	 */
	@NonNull
	String generate(JoinPoint joinPoint, Idempotent idempotentAnnotation);

}
