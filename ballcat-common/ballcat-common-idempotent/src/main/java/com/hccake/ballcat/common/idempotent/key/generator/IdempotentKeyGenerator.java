package com.hccake.ballcat.common.idempotent.key.generator;

import com.hccake.ballcat.common.idempotent.annotation.Idempotent;
import org.aspectj.lang.JoinPoint;
import org.springframework.lang.NonNull;

/**
 * 幂等key生成器
 *
 * @author lishangbu
 * @date 2022/10/18
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
