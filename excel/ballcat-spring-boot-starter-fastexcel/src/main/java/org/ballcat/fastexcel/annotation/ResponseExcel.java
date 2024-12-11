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

import cn.idev.excel.converters.Converter;
import cn.idev.excel.support.ExcelTypeEnum;
import cn.idev.excel.write.handler.WriteHandler;
import org.ballcat.fastexcel.fill.FillDataSupplier;
import org.ballcat.fastexcel.head.HeadGenerator;

/**
 * `@ResponseExcel 注解`
 *
 * @author lengleng
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseExcel {

	/**
	 * 文件名称
	 * @return string
	 */
	String name() default "";

	/**
	 * 文件类型 （xlsx xls）
	 * @return string
	 */
	ExcelTypeEnum suffix() default ExcelTypeEnum.XLSX;

	/**
	 * 文件密码
	 * @return password
	 */
	String password() default "";

	/**
	 * sheet 名称，支持多个
	 * @return String[]
	 */
	Sheet[] sheets() default {};

	/**
	 * 内存操作
	 */
	boolean inMemory() default false;

	/**
	 * excel 模板
	 * @return String
	 */
	String template() default "";

	/**
	 * 填充数据提供器
	 * @return HeadGenerator
	 */
	Class<? extends FillDataSupplier> fillDataSupplier() default FillDataSupplier.class;

	/**
	 * + 包含字段
	 * @return String[]
	 */
	String[] include() default {};

	/**
	 * 排除字段
	 * @return String[]
	 */
	String[] exclude() default {};

	/**
	 * 拦截器，自定义样式等处理器
	 * @return WriteHandler[]
	 */
	Class<? extends WriteHandler>[] writeHandler() default {};

	/**
	 * 转换器
	 * @return Converter[]
	 */
	Class<? extends Converter>[] converter() default {};

	/**
	 * 自定义Excel头生成器
	 * @return HeadGenerator
	 */
	Class<? extends HeadGenerator> headGenerator() default HeadGenerator.class;

	/**
	 * excel 头信息国际化
	 * @return boolean
	 */
	boolean i18nHeader() default false;

	/**
	 * 填充模式
	 * @return boolean
	 */
	boolean fill() default false;

}
