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

package org.ballcat.log.operation;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.log.operation.annotation.OperationLog;
import org.ballcat.log.operation.annotation.OperationLogFinder;
import org.ballcat.log.operation.domain.OperationLogInfo;
import org.ballcat.log.operation.enums.OperationStatusEnum;
import org.ballcat.log.operation.expression.OperationLogExpressionEvaluator;
import org.ballcat.log.operation.handler.OperationLogHandler;
import org.ballcat.log.operation.provider.HttpInfoProvider;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * 操作日志切面类
 *
 * @author Hccake 2019/10/15 18:16
 * @since 2.0.0
 */
@Slf4j
public class OperationLogMethodInterceptor implements MethodInterceptor, BeanFactoryAware {

	private final OperationLogExpressionEvaluator expressionEvaluator;

	@Nullable
	private BeanFactory beanFactory;

	@Setter
	private Map<Class<?>, String> paramReplacements;

	public OperationLogMethodInterceptor(OperationLogExpressionEvaluator expressionEvaluator) {
		this(expressionEvaluator, defaultReplaceParamsMap());
	}

	public OperationLogMethodInterceptor(OperationLogExpressionEvaluator expressionEvaluator,
			Map<Class<?>, String> paramReplacements) {
		this.expressionEvaluator = expressionEvaluator;
		this.paramReplacements = paramReplacements;
	}

	private static Map<Class<?>, String> defaultReplaceParamsMap() {
		Map<Class<?>, String> replaceParamsMap = new HashMap<>(8);
		replaceParamsMap.put(MultipartFile.class, "[MultipartFile]");
		replaceParamsMap.put(ServletRequest.class, "[ServletRequest]");
		replaceParamsMap.put(ServletResponse.class, "[ServletResponse]");
		return replaceParamsMap;
	}

	/**
	 * 参考 spring cache 处理逻辑，抽象一层方便后续切换底层 aop 实现
	 * @param invocation 方法调用
	 * @return Object
	 * @throws Throwable 异常
	 * @see CacheInterceptor
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();

		OperationInvoker aopAllianceInvoker = () -> {
			try {
				return invocation.proceed();
			}
			catch (Throwable ex) {
				throw new OperationInvoker.ThrowableWrapper(ex);
			}
		};

		try {
			return execute(aopAllianceInvoker, invocation.getThis(), method, invocation.getArguments());
		}
		catch (OperationInvoker.ThrowableWrapper th) {
			throw th.getOriginal();
		}
	}

	public Object execute(OperationInvoker operationInvoker, Object target, Method method, Object[] args) {

		long startTime = System.currentTimeMillis();
		LocalDateTime operationTime = LocalDateTime.now();

		// 获取目标方法
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
		Method targetMethod = (!Proxy.isProxyClass(targetClass) ? AopUtils.getMostSpecificMethod(method, targetClass)
				: method);

		// 获取操作日志注解 备注： AnnotationUtils.findAnnotation 方法无法获取继承的属性
		OperationLog operationLogging = OperationLogFinder.findOperationLog(targetMethod, targetClass);
		Assert.notNull(operationLogging, "operationLogging annotation must not be null!");

		Throwable throwable = null;
		Object methodResult = null;

		// push本地变量
		OperationLogContextHolder.pushLocalVariableMap();

		try {
			methodResult = operationInvoker.invoke();
			return methodResult;
		}
		catch (OperationInvoker.ThrowableWrapper th) {
			throwable = th.getOriginal();
			throw th;
		}
		finally {
			try {
				// 填充默认变量
				fillContextVariable(methodResult, throwable);
				// 处理操作日志
				processOperationLog(target, args, targetMethod, targetClass, operationLogging, operationTime, startTime,
						throwable, methodResult);
			}
			catch (Exception e) {
				log.error("记录操作日志失败", e);
			}
			finally {
				// 弹出本地变量
				OperationLogContextHolder.popLocalVariableMap();
			}
		}
	}

	protected void fillContextVariable(Object methodResult, Throwable throwable) {
		OperationLogContextHolder.putLocalVariable("_ret", methodResult);
		if (throwable != null) {
			OperationLogContextHolder.putLocalVariable("_errorMsg", throwable.getMessage());
		}
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			OperationLogContextHolder.putLocalVariable("_request", request);
		}
	}

	protected void processOperationLog(Object target, Object[] args, Method targetMethod, Class<?> targetClass,
			OperationLog operationLogging, LocalDateTime operationTime, long startTime, Throwable throwable,
			Object methodResult) {
		// 根据当前切点，获取到 spEL 上下文
		EvaluationContext evaluationContext = this.expressionEvaluator.createEvaluationContext(target, targetMethod,
				args, this.beanFactory);

		AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(targetMethod, targetClass);
		OperationLogExpressionParser expressionParser = new OperationLogExpressionParser(annotatedElementKey,
				evaluationContext);

		// 判断是否需要进行操作日志记录
		boolean condition = expressionParser.parseCondition(operationLogging.condition());
		if (!condition) {
			return;
		}

		OperationLogInfo operationLogInfo = buildOperationLogInfo(args, targetMethod, targetClass, operationLogging,
				operationTime, startTime, throwable, methodResult, expressionParser);

		if (this.beanFactory != null) {
			Class<? extends OperationLogHandler> logHandlerClass = operationLogging.logHandler();
			OperationLogHandler operationLogHandler = this.beanFactory.getBean(logHandlerClass);
			operationLogHandler.handle(operationLogInfo);
		}

	}

	protected OperationLogInfo buildOperationLogInfo(Object[] args, Method targetMethod, Class<?> targetClass,
			OperationLog operationLogging, LocalDateTime operationTime, long startTime, Throwable throwable,
			Object methodResult, OperationLogExpressionParser expressionParser) {
		OperationLogInfo operationLogInfo = new OperationLogInfo().setOperationTime(operationTime)
			.setBizType(operationLogging.bizType())
			.setSubType(operationLogging.subType())
			.setClassName(targetClass.getName())
			.setMethodName(targetMethod.getName())
			.setExecutionTime(System.currentTimeMillis() - startTime);

		String bizNo = expressionParser.parseStringValue(operationLogging.bizNo());
		operationLogInfo.setBizNo(bizNo);

		String operator = expressionParser.parseStringValue(operationLogging.operator());
		operationLogInfo.setOperator(operator);

		String extra = expressionParser.parseStringValue(operationLogging.extra());
		operationLogInfo.setExtra(extra);

		String traceId = expressionParser.parseStringValue(operationLogging.traceId());
		operationLogInfo.setTraceId(traceId);

		if (this.beanFactory != null) {
			Class<? extends HttpInfoProvider> httpInfoProviderClass = operationLogging.httpInfoProvider();
			HttpInfoProvider httpInfoProvider = this.beanFactory.getBean(httpInfoProviderClass);
			OperationLogInfo.HttpInfo httpInfo = httpInfoProvider.get();
			operationLogInfo.setHttpInfo(httpInfo);
		}

		// 记录方法入参
		if (operationLogging.isRecordArgs()) {
			// 替换文件等无法序列化的参数
			List<Object> arguments = new ArrayList<>();
			for (Object arg : args) {
				Object argument = null;
				if (arg != null) {
					argument = arg;
					for (Map.Entry<Class<?>, String> entry : this.paramReplacements.entrySet()) {
						if (entry.getKey().isAssignableFrom(arg.getClass())) {
							argument = entry.getValue();
							break;
						}
					}
				}
				arguments.add(argument);
			}
			operationLogInfo.setMethodArgs(JsonUtils.toJson(arguments));
		}

		// 如果有异常，则记录异常信息 + 最后 3 行堆栈
		if (throwable != null) {
			StringBuilder sb = new StringBuilder(throwable.getMessage() == null ? "" : throwable.getMessage());
			StackTraceElement[] stackTrace = throwable.getStackTrace();
			if (stackTrace.length > 0) {
				for (int i = 0; i < 3 && i < stackTrace.length; i++) {
					sb.append("\n\t").append(stackTrace[i].toString());
				}
			}
			operationLogInfo.setErrorStack(sb.toString());
			String errorMessage = expressionParser.parseStringValue(operationLogging.errorMessage());
			operationLogInfo.setMessage(errorMessage);
			operationLogInfo.setStatus(OperationStatusEnum.EXECUTE_ERROR.getValue());
		}
		else {
			boolean successCondition = expressionParser.parseCondition(operationLogging.successCondition());
			if (successCondition) {
				String successMessage = expressionParser.parseStringValue(operationLogging.successMessage());
				operationLogInfo.setMessage(successMessage);
				operationLogInfo.setStatus(OperationStatusEnum.SUCCESS.getValue());
			}
			else {
				String failureMessage = expressionParser.parseStringValue(operationLogging.failureMessage());
				operationLogInfo.setMessage(failureMessage);
				operationLogInfo.setStatus(OperationStatusEnum.FAILURE.getValue());
			}

			// 记录方法返回值
			if (operationLogging.isRecordResult()) {
				operationLogInfo.setMethodResult(JsonUtils.toJson(methodResult));
			}
		}

		return operationLogInfo;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Getter
	@RequiredArgsConstructor
	protected class OperationLogExpressionParser {

		private final AnnotatedElementKey annotatedElementKey;

		private final EvaluationContext evaluationContext;

		public boolean parseCondition(String condition) {
			if (StringUtils.hasText(condition)) {
				return parseBooleanValue(condition);
			}
			return true;
		}

		public String parseStringValue(String expression) {
			if (!StringUtils.hasText(expression)) {
				return expression;
			}
			Object object = OperationLogMethodInterceptor.this.expressionEvaluator.parseExpression(expression,
					this.annotatedElementKey, this.evaluationContext);
			return object == null ? null : object.toString();
		}

		public boolean parseBooleanValue(String expression) {
			return Boolean.TRUE.equals(OperationLogMethodInterceptor.this.expressionEvaluator
				.parseExpression(expression, this.annotatedElementKey, this.evaluationContext));
		}

	}

}
