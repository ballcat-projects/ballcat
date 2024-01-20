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

package org.ballcat.web.exception.handler;

import org.ballcat.common.core.exception.handler.GlobalExceptionHandler;

/**
 * 默认的异常日志处理类
 *
 * @author Hccake 2019/10/18 17:06
 */
public class DoNothingGlobalExceptionHandler implements GlobalExceptionHandler {

	/**
	 * 在此处理日志 默认什么都不处理
	 * @param throwable 异常信息
	 */
	@Override
	public void handle(Throwable throwable) {
		// do nothing
	}

}
