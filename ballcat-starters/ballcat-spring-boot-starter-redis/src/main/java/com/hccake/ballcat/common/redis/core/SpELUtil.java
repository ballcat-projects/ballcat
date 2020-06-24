package com.hccake.ballcat.common.redis.core;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/3 10:29
 */
public class SpELUtil {

	/**
	 * SpEL 解析器
	 */
	public static final ExpressionParser PARSER = new SpelExpressionParser();

	/**
	 *
	 *
	 * 方法参数获取
	 */
	public static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

	/**
	 * 支持 #p0 参数索引的表达式解析
	 * @param rootObject 根对象,method 所在的对象
	 * @param spEL 表达式
	 * @param method 目标方法
	 * @param args 方法入参
	 * @return 解析后的字符串
	 */
	public static String parseValueToString(Object rootObject, Method method, Object[] args, String spEL) {

		StandardEvaluationContext context = getSpElContext(rootObject, method, args);
		return parseValueToString(context, spEL);
	}

	/**
	 * 支持 #p0 参数索引的表达式解析
	 * @param rootObject 根对象,method 所在的对象
	 * @param method ，目标方法
	 * @param args 方法入参
	 * @return 解析后的字符串
	 */
	public static StandardEvaluationContext getSpElContext(Object rootObject, Method method, Object[] args) {

		String[] paraNameArr = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
		// SPEL 上下文
		StandardEvaluationContext context = new MethodBasedEvaluationContext(rootObject, method, args,
				PARAMETER_NAME_DISCOVERER);
		// 把方法参数放入 SPEL 上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		return context;
	}

	public static String parseValueToString(StandardEvaluationContext context, String spEL) {
		return PARSER.parseExpression(spEL).getValue(context, String.class);
	}

}
