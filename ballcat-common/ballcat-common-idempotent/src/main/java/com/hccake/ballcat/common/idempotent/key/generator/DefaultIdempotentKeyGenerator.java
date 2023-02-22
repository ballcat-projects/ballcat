package com.hccake.ballcat.common.idempotent.key.generator;

import com.hccake.ballcat.common.idempotent.annotation.Idempotent;
import com.hccake.ballcat.common.util.SpelUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 默认幂等key生成器
 *
 * @author lishangbu
 * @date 2022/10/18
 */
public class DefaultIdempotentKeyGenerator implements IdempotentKeyGenerator {

	/**
	 * 生成幂等 key
	 * @param joinPoint 切点
	 * @param idempotentAnnotation 幂等注解
	 * @return String 幂等标识
	 */
	@Override
	public String generate(JoinPoint joinPoint, Idempotent idempotentAnnotation) {
		String uniqueExpression = idempotentAnnotation.uniqueExpression();
		// 如果没有填写表达式，直接返回 prefix
		if ("".equals(uniqueExpression)) {
			return idempotentAnnotation.prefix();
		}

		// 获取当前方法以及方法参数
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Object[] args = joinPoint.getArgs();

		// 根据当前切点，获取到 spEL 上下文
		StandardEvaluationContext spelContext = SpelUtils.getSpelContext(joinPoint.getTarget(), method, args);
		// 如果在 servlet 环境下，则将 request 信息放入上下文，便于获取请求参数
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
			.getRequestAttributes();
		if (requestAttributes != null) {
			spelContext.setVariable(RequestAttributes.REFERENCE_REQUEST, requestAttributes.getRequest());
		}
		// 解析出唯一标识
		String uniqueStr = SpelUtils.parseValueToString(spelContext, uniqueExpression);
		// 和 prefix 拼接获得完整的 key
		return idempotentAnnotation.prefix() + ":" + uniqueStr;
	}

}
