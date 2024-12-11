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

package org.ballcat.fastexcel.handler;

import javax.servlet.http.HttpServletResponse;

import org.ballcat.fastexcel.annotation.ResponseExcel;

/**
 * sheet 写出处理器
 *
 * @author lengleng 2020/3/29
 */
public interface SheetWriteHandler {

	/**
	 * 是否支持
	 * @param obj 返回对象
	 * @return boolean
	 */
	boolean support(Object obj);

	/**
	 * 校验
	 * @param responseExcel 注解
	 */
	void check(ResponseExcel responseExcel);

	/**
	 * 返回的对象
	 * @param o obj
	 * @param response 输出对象
	 * @param responseExcel 注解
	 */
	void export(Object o, HttpServletResponse response, ResponseExcel responseExcel);

	/**
	 * 写成对象
	 * @param o obj
	 * @param response 输出对象
	 * @param responseExcel 注解
	 */
	void write(Object o, HttpServletResponse response, ResponseExcel responseExcel);

}
