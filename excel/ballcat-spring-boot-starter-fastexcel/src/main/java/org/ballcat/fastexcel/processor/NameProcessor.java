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

package org.ballcat.fastexcel.processor;

import java.lang.reflect.Method;

/**
 * @author lengleng 2020/3/29
 */
public interface NameProcessor {

	/**
	 * 解析名称
	 * @param args 拦截器对象
	 * @param method 当前拦截方法
	 * @param key 表达式
	 * @return String 根据表达式解析后的字符串
	 */
	String doDetermineName(Object[] args, Method method, String key);

}
