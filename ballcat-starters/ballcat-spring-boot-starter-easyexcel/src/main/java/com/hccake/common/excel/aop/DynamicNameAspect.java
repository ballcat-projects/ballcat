package com.hccake.common.excel.aop;

import com.hccake.common.excel.annotation.ResponseExcel;
import com.hccake.common.excel.kit.ExcelNameContextHolder;
import com.hccake.common.excel.processor.NameProcessor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2020/3/29
 */
@Aspect
@Component
@RequiredArgsConstructor
public class DynamicNameAspect {

	private final NameProcessor processor;

	@Before("@annotation(excel)")
	public void before(JoinPoint point, ResponseExcel excel) {
		MethodSignature ms = (MethodSignature) point.getSignature();
		String name = processor.doDetermineName(point.getArgs(), ms.getMethod(), excel.name());
		ExcelNameContextHolder.set(name);
	}

	@AfterThrowing("@annotation(com.hccake.common.excel.annotation.ResponseExcel)")
	public void afterThrowing() {
		// 防止异常导致数据未清空
		ExcelNameContextHolder.clear();
	}

}
