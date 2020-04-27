package com.hccake.common.excel.aop;


import com.hccake.common.excel.annotation.ResponseExcel;
import com.hccake.common.excel.kit.ExcelNameContextHolder;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import com.hccake.common.excel.processor.NameProcessor;

/**
 * @author lengleng
 * @date 2020/3/29
 */
@Aspect
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class DynamicNameAspect {
	private final NameProcessor processor;

	@Before("@annotation(excel)")
	public void around(JoinPoint point, ResponseExcel excel) {
		MethodSignature ms = (MethodSignature) point.getSignature();
		String name = processor.doDetermineName(point.getArgs(), ms.getMethod(), excel.name());
		ExcelNameContextHolder.set(name);
	}
}
