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

package org.ballcat.common.core.compose;

/**
 * 上下文组件, 在接入对应的上下文时(如: spring 的 bean) 便于在 开始和结束时执行对应的方法
 * <p>
 * 默认自动接入spring
 * </p>
 * <p>
 * 一般用于线程类实例达成接入到对应的上下文环境时自动开启和结束线程
 * </p>
 *
 * @author lingting 2022/10/15 17:55
 */
public interface ContextComponent {

	/**
	 * 上下文准备好之后调用, 内部做一些线程的初始化以及线程启动
	 */
	void onApplicationStart();

	/**
	 * 在上下文销毁前调用, 内部做线程停止和数据缓存相关
	 */
	void onApplicationStop();

}
