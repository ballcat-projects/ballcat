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

package org.ballcat.common.core.exception.handler;

/**
 * 异常日志处理类
 *
 * @author Hccake 2019/10/18 17:05
 */
public interface GlobalExceptionHandler {

	/**
	 * 在此处理错误信息 进行落库，入ES， 发送报警通知等信息
	 * @param throwable 异常
	 */
	void handle(Throwable throwable);

}
