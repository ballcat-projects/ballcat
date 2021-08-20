package com.hccake.ballcat.common.log.operation.handler;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.hccake.ballcat.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hccake
 */
@Slf4j
public abstract class AbstractOperationLogHandler<T> implements OperationLogHandler<T> {

	/**
	 * <p>
	 * 忽略记录的参数类型列表
	 * </p>
	 * 忽略判断时只针对方法入参类型，如果入参为对象，其某个属性需要忽略的无法处理，可以使用 @JsonIgnore 进行忽略。
	 */
	private final List<Class<?>> ignoredParamClasses = ListUtil.toList(ServletRequest.class, ServletResponse.class,
			MultipartFile.class);

	/**
	 * 添加忽略记录的参数类型
	 * @param clazz 参数类型
	 */
	public void addIgnoredParamClass(Class<?> clazz) {
		ignoredParamClasses.add(clazz);
	}

	/**
	 * 获取方法参数
	 * @param joinPoint 切点
	 * @return 当前方法入参的Json Str
	 */
	public String getParams(ProceedingJoinPoint joinPoint) {
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
			if (arg == null) {
				paramsMap.put(parameterNames[i], null);
				continue;
			}
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
