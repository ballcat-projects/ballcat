package com.hccake.ballcat.commom.log.operation.aspect;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.URLUtil;
import com.hccake.ballcat.commom.log.constant.LogConstant;
import com.hccake.ballcat.commom.log.operation.annotation.OperationLogging;
import com.hccake.ballcat.commom.log.operation.enums.LogStatusEnum;
import com.hccake.ballcat.commom.log.operation.event.OperationLogEvent;
import com.hccake.ballcat.commom.log.operation.model.OperationLogDTO;
import com.hccake.ballcat.commom.log.util.LogUtils;
import com.hccake.ballcat.common.util.IpUtils;
import com.hccake.ballcat.common.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:16 操作日志切面类
 */
@Slf4j
@Aspect
@Order(0)
@RequiredArgsConstructor
public class OperationLogAspect {

	private final ApplicationEventPublisher publisher;

	private final List<Class<?>> ignoredParamClasses = Arrays.asList(ServletRequest.class, ServletResponse.class,
			MultipartFile.class);

	/**
	 * 添加忽略记录的参数类型
	 * @param clazz 参数类型
	 */
	public void addIgnoredParamClass(Class<?> clazz) {
		ignoredParamClasses.add(clazz);
	}

	@Around("execution(@(@com.hccake.ballcat.commom.log.operation.annotation.OperationLogging *) * *(..)) "
			+ "|| @annotation(com.hccake.ballcat.commom.log.operation.annotation.OperationLogging)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		// 开始时间
		long startTime = System.currentTimeMillis();

		// 获取目标方法
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		// 获取操作日志注解 备注： AnnotationUtils.findAnnotation 方法无法获取继承的属性
		OperationLogging operationLogging = AnnotatedElementUtils.findMergedAnnotation(method, OperationLogging.class);

		// 获取操作日志 DTO
		Assert.notNull(operationLogging, "operationLogging annotation must not be null!");
		OperationLogDTO operationLogDTO = this.initOperationLog(operationLogging, joinPoint);

		try {
			return joinPoint.proceed();
		}
		catch (Throwable throwable) {
			operationLogDTO.setStatus(LogStatusEnum.FAIL.getValue());
			throw throwable;
		}
		finally {
			// 结束时间
			operationLogDTO.setTime(System.currentTimeMillis() - startTime);
			// 发布事件
			publisher.publishEvent(new OperationLogEvent(operationLogDTO));
		}
	}

	/**
	 * 根据请求生成操作日志
	 * @param operationLogging 操作日志注解
	 * @param joinPoint 切点
	 * @return 初始化一个操作日志DTO
	 */
	private OperationLogDTO initOperationLog(OperationLogging operationLogging, ProceedingJoinPoint joinPoint) {
		// 获取 Request
		HttpServletRequest request = LogUtils.getHttpServletRequest();

		// @formatter:off
		return new OperationLogDTO()
				.setCreateTime(LocalDateTime.now())
				.setIp(IpUtils.getIpAddr(request))
				.setMethod(request.getMethod())
				.setOperator(Objects.requireNonNull(LogUtils.getUsername()))
				.setStatus(LogStatusEnum.SUCCESS.getValue())
				.setUserAgent(request.getHeader("user-agent"))
				.setUri(URLUtil.getPath(request.getRequestURI()))
				.setType(operationLogging.type().getValue())
				.setMsg(operationLogging.msg())
				.setParams(getParams(joinPoint))
				.setTraceId(MDC.get(LogConstant.TRACE_ID));
		// @formatter:on
	}

	/**
	 * 获取方法参数
	 * @param joinPoint 切点
	 * @return 当前方法入参的Json Str
	 */
	private String getParams(ProceedingJoinPoint joinPoint) {
		// 获取方法签名
		Signature signature = joinPoint.getSignature();
		String strClassName = joinPoint.getTarget().getClass().getName();
		String strMethodName = signature.getName();
		MethodSignature methodSignature = (MethodSignature) signature;
		log.debug("[getParams]，获取方法参数[类名]:{},[方法]:{}", strClassName, strMethodName);

		String[] parameterNames = methodSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();
		if (ArrayUtil.isEmpty(parameterNames)) {
			return null;
		}
		Map<String, Object> paramsMap = new HashMap<>();
		for (int i = 0; i < parameterNames.length; i++) {
			Object arg = args[i];
			Class<?> argClass = arg.getClass();
			// 忽略部分类型的参数记录
			for (Class<?> ignoredParamClass : ignoredParamClasses) {
				if (ignoredParamClass.isAssignableFrom(argClass)) {
					arg = "ignored param type: " + argClass;
					break;
				}
			}
			paramsMap.put(parameterNames[i], arg);
		}

		String params = "";
		try {
			// 入参类中的属性可以通过注解进行数据落库脱敏以及忽略等操作
			params = JsonUtils.toJson(paramsMap);
		}
		catch (Exception e) {
			log.error("[getParams]，获取方法参数异常，[类名]:{},[方法]:{}", strClassName, strMethodName, e);
		}

		return params;
	}

}
