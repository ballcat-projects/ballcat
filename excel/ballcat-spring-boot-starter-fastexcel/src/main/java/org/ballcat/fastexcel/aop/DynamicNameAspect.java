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

package org.ballcat.fastexcel.aop;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.processor.NameProcessor;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author lengleng 2020/3/29
 */
@Aspect
@RequiredArgsConstructor
public class DynamicNameAspect {

	public static final String EXCEL_NAME_KEY = "__EXCEL_NAME_KEY__";

	private final NameProcessor processor;

	@Before("@annotation(excel)")
	public void before(JoinPoint point, ResponseExcel excel) {
		MethodSignature ms = (MethodSignature) point.getSignature();

		String name = excel.name();
		// 当配置的 excel 名称为空时，取当前时间
		if (!StringUtils.hasText(name)) {
			name = LocalDateTime.now().toString();
		}
		else {
			name = this.processor.doDetermineName(point.getArgs(), ms.getMethod(), excel.name());
		}

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Objects.requireNonNull(requestAttributes).setAttribute(EXCEL_NAME_KEY, name, RequestAttributes.SCOPE_REQUEST);
	}

}
