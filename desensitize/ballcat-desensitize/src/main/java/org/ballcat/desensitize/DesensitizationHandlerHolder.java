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

package org.ballcat.desensitize;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import org.ballcat.desensitize.handler.DesensitizationHandler;
import org.ballcat.desensitize.handler.IndexDesensitizationHandler;
import org.ballcat.desensitize.handler.RegexDesensitizationHandler;
import org.ballcat.desensitize.handler.SimpleDesensitizationHandler;
import org.ballcat.desensitize.handler.SlideDesensitizationHandler;

/**
 * 脱敏处理器持有者
 * <p>
 * - 默认提供 Regex 和 Slide 类型的脱敏处理器 <br/>
 * - Simple 脱敏处理器则使用SPI方式加载，便于用户扩展处理
 * </p>
 *
 * @author Hccake 2021/1/22
 *
 */
public final class DesensitizationHandlerHolder {

	private static final DesensitizationHandlerHolder INSTANCE = new DesensitizationHandlerHolder();

	private final Map<Class<? extends DesensitizationHandler>, DesensitizationHandler> desensitizationHandlerMap;

	private DesensitizationHandlerHolder() {
		this.desensitizationHandlerMap = new ConcurrentHashMap<>(16);
		// 滑动脱敏处理器
		this.desensitizationHandlerMap.put(SlideDesensitizationHandler.class, new SlideDesensitizationHandler());
		// 正则脱敏处理器
		this.desensitizationHandlerMap.put(RegexDesensitizationHandler.class, new RegexDesensitizationHandler());
		// 基于规则脱敏处理器
		this.desensitizationHandlerMap.put(IndexDesensitizationHandler.class, new IndexDesensitizationHandler());
		// SPI 加载所有的 Simple脱敏类型处理
		ServiceLoader<SimpleDesensitizationHandler> loadedDrivers = ServiceLoader
			.load(SimpleDesensitizationHandler.class);
		for (SimpleDesensitizationHandler desensitizationHandler : loadedDrivers) {
			this.desensitizationHandlerMap.put(desensitizationHandler.getClass(), desensitizationHandler);
		}
	}

	/**
	 * 获取 DesensitizationHandler
	 * @return 处理器实例
	 */
	public static DesensitizationHandler getDesensitizationHandler(
			Class<? extends DesensitizationHandler> handlerClass) {
		return INSTANCE.desensitizationHandlerMap.get(handlerClass);
	}

	/**
	 * 获取 RegexDesensitizationHandler
	 * @return 处理器实例
	 */
	public static RegexDesensitizationHandler getRegexDesensitizationHandler() {
		return (RegexDesensitizationHandler) INSTANCE.desensitizationHandlerMap.get(RegexDesensitizationHandler.class);
	}

	/**
	 * 获取 SlideDesensitizationHandler
	 * @return 处理器实例
	 */
	public static SlideDesensitizationHandler getSlideDesensitizationHandler() {
		return (SlideDesensitizationHandler) INSTANCE.desensitizationHandlerMap.get(SlideDesensitizationHandler.class);
	}

	/**
	 * 获取 RuleDesensitizationHandler
	 * @return 处理器实例
	 */
	public static IndexDesensitizationHandler getIndexDesensitizationHandler() {
		return (IndexDesensitizationHandler) INSTANCE.desensitizationHandlerMap.get(IndexDesensitizationHandler.class);
	}

	/**
	 * 获取指定的 SimpleDesensitizationHandler
	 * @param handlerClass SimpleDesensitizationHandler的实现类
	 * @return 处理器实例
	 */
	public static SimpleDesensitizationHandler getSimpleDesensitizationHandler(
			Class<? extends SimpleDesensitizationHandler> handlerClass) {
		return (SimpleDesensitizationHandler) INSTANCE.desensitizationHandlerMap.get(handlerClass);
	}

	/**
	 * 添加Handler
	 * @param handlerClass DesensitizationHandler的实现类
	 * @param handler 处理器实例
	 */
	public static void addDesensitizationHandler(Class<? extends DesensitizationHandler> handlerClass,
			DesensitizationHandler handler) {
		INSTANCE.desensitizationHandlerMap.put(handlerClass, handler);
	}

}
