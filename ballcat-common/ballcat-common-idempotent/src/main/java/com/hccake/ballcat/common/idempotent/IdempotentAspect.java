package com.hccake.ballcat.common.idempotent;

import cn.hutool.core.lang.Assert;
import com.hccake.ballcat.common.idempotent.annotation.Idempotent;
import com.hccake.ballcat.common.idempotent.exception.IdempotentException;
import com.hccake.ballcat.common.idempotent.key.IdempotentKeyStore;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.util.SpelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * @author hccake
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class IdempotentAspect {

	private final IdempotentKeyStore idempotentKeyStore;

	@Around("@annotation(idempotentAnnotation)")
	public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotentAnnotation) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Object[] args = joinPoint.getArgs();

		// 获取幂等标识
		String idempotentKey = buildIdempotentKey(joinPoint, idempotentAnnotation, method, args);

		// 校验当前请求是否重复请求
		Assert.isTrue(idempotentKeyStore.saveIfAbsent(idempotentKey, idempotentAnnotation.duration()), () -> {
			String errorMessage = String.format("拒绝重复执行方法[%s], 幂等key:[%s]", method.getName(), idempotentKey);
			throw new IdempotentException(BaseResultCode.REPEATED_EXECUTE.getCode(), errorMessage);
		});

		try {
			Object result = joinPoint.proceed();
			if (idempotentAnnotation.removeKeyWhenFinished()) {
				idempotentKeyStore.remove(idempotentKey);
			}
			return result;
		}
		catch (Throwable e) {
			// 异常时必须删除，方便重试处理
			idempotentKeyStore.remove(idempotentKey);
			throw e;
		}

	}

	/**
	 * 构建幂等标识 key
	 * @param joinPoint 切点
	 * @param idempotentAnnotation 幂等注解
	 * @param method 当前方法
	 * @param args 方法参数
	 * @return String 幂等标识
	 */
	private String buildIdempotentKey(ProceedingJoinPoint joinPoint, Idempotent idempotentAnnotation, Method method,
			Object[] args) {
		String uniqueExpression = idempotentAnnotation.uniqueExpression();
		// 如果没有填写表达式，直接返回 prefix
		if ("".equals(uniqueExpression)) {
			return idempotentAnnotation.prefix();
		}

		// 根据当前切点，获取到 spEL 上下文
		StandardEvaluationContext spelContext = SpelUtils.getSpelContext(joinPoint.getTarget(), method, args);
		// 如果在 sevlet 环境下，则将 request 信息放入上下文，便于获取请求参数
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		if (requestAttributes != null) {
			spelContext.setVariable("request", requestAttributes.getRequest());
		}
		// 解析出唯一标识
		String uniqueStr = SpelUtils.parseValueToString(spelContext, uniqueExpression);
		// 和 prefix 拼接获得完整的 key
		return idempotentAnnotation.prefix() + ":" + uniqueStr;
	}

}
