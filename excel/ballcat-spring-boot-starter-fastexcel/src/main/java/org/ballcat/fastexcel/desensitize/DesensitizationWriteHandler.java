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

package org.ballcat.fastexcel.desensitize;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import cn.idev.excel.metadata.property.ExcelContentProperty;
import cn.idev.excel.write.handler.CellWriteHandler;
import cn.idev.excel.write.handler.context.CellWriteHandlerContext;
import org.apache.poi.ss.usermodel.Cell;
import org.ballcat.desensitize.annotation.AnnotationDesensitizeFunction;
import org.ballcat.desensitize.annotation.AnnotationHandlerHolder;

/**
 * 脱敏拦截器。
 *
 * @author Hccake
 */
public class DesensitizationWriteHandler implements CellWriteHandler {

	@Override
	public void afterCellDispose(CellWriteHandlerContext context) {
		Cell cell = context.getCell();

		ExcelContentProperty excelContentProperty = context.getExcelContentProperty();
		Field field = excelContentProperty.getField();
		if (field == null) {
			return;
		}

		// 获取脱敏注解
		Annotation desensitizeAnnotation = getDesensitizeAnnotation(field);
		if (desensitizeAnnotation == null) {
			return;
		}

		// 获取脱敏方法
		AnnotationDesensitizeFunction handleFunction = AnnotationHandlerHolder
			.getHandleFunction(desensitizeAnnotation.annotationType());
		String desensitizedCellValue = handleFunction.mask(desensitizeAnnotation, cell.getStringCellValue());
		cell.setCellValue(desensitizedCellValue);
	}

	/**
	 * 得到脱敏注解
	 * @param field 字段
	 * @return 返回第一个获取的脱敏注解
	 */
	private Annotation getDesensitizeAnnotation(Field field) {
		for (Class<? extends Annotation> annotationClass : AnnotationHandlerHolder.getAnnotationClasses()) {
			Annotation annotation = field.getAnnotation(annotationClass);
			if (annotation != null) {
				return annotation;
			}
		}
		return null;
	}

}
