package com.hccake.ballcat.common.idempotent.key.generator;

import com.hccake.ballcat.common.idempotent.annotation.Idempotent;
import org.aspectj.lang.JoinPoint;

/**
 * 幂等key生成器
 *
 * @author lishangbu
 * @date 2022/10/18
 */
public interface KeyGenerator {

	/**
	 * 生成幂等 key
	 * @param joinPoint 切点
	 * @param idempotentAnnotation 幂等注解
	 * @return String 幂等标识
	 */
	String generate(JoinPoint joinPoint, Idempotent idempotentAnnotation);

}
