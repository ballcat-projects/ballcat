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

package org.ballcat.fastexcel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.idev.excel.read.builder.AbstractExcelReaderParameterBuilder;
import org.ballcat.fastexcel.handler.DefaultAnalysisEventListener;
import org.ballcat.fastexcel.handler.ListAnalysisEventListener;

/**
 * 导入excel
 *
 * @author lengleng 2021/4/16
 * @author L.cm
 */
@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestExcel {

	/**
	 * 前端上传字段名称 file
	 */
	String fileName() default "file";

	/**
	 * 读取的监听器类
	 * @return readListener
	 */
	Class<? extends ListAnalysisEventListener<?>> readListener() default DefaultAnalysisEventListener.class;

	/**
	 * 是否跳过空行
	 * @return 默认跳过
	 */
	boolean ignoreEmptyRow() default true;

	/**
	 * 工作表名称
	 * @return sheetName
	 */
	String sheetName() default "";

	/**
	 * Count the number of added heads when read sheet. 0 - This Sheet has no head ,since
	 * the first row are the data 1 - This Sheet has one row head , this is the default 2
	 * - This Sheet has two row head ,since the third row is the data
	 * @see AbstractExcelReaderParameterBuilder#headRowNumber
	 * @return headRowNumber
	 */
	int headRowNumber() default 1;

	/**
	 * The number of rows to read, the default is all, start with 0.
	 */
	int numRows() default -1;

}
