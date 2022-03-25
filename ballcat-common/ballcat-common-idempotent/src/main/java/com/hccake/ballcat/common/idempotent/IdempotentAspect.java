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
import org.springframework.web.context.request.RequestAttributes;
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
		// 获取幂等标识
		String idempotentKey = buildIdempotentKey(joinPoint, idempotentAnnotation);

		// 校验当前请求是否重复请求
		boolean saveSuccess = idempotentKeyStore.saveIfAbsent(idempotentKey, idempotentAnnotation.duration(),
				idempotentAnnotation.timeUnit());
		Assert.isTrue(saveSuccess, () -> {
			throw new IdempotentException(BaseResultCode.REPEATED_EXECUTE.getCode(), idempotentAnnotation.message());
		});

		try {
			Object result = joinPoint.proceed();
			if (idempotentAnnotation.removeKeyWhenFinished()) {
				idempotentKeyStore.remove(idempotentKey);
			}
			return result;
		}
		catch (Throwable e) {
			// 异常时，根据配置决定是否删除幂等 key
			if (idempotentAnnotation.removeKeyWhenError()) {
				idempotentKeyStore.remove(idempotentKey);
			}
			throw e;
		}

	}

	/**
	 * 构建幂等标识 key
	 * @param joinPoint 切点
	 * @param idempotentAnnotation 幂等注解
	 * @return String 幂等标识
	 */
	private String buildIdempotentKey(ProceedingJoinPoint joinPoint, Idempotent idempotentAnnotation) {
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
