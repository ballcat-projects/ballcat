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

package org.ballcat.log.operation;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.util.Assert;

/**
 * 操作日志上下文持有者，用于管理操作日志的上下文.
 *
 * @author Hccake
 * @since 2.0.0
 */
public final class OperationLogContextHolder {

	private OperationLogContextHolder() {
	}

	/**
	 * 本地上下文，用于存储每层方法调用的数据。
	 */
	private static final ThreadLocal<Deque<Map<String, Object>>> LOCAL_VARIABLE_MAP_STACK = new ThreadLocal<>();

	/**
	 * 全局上下文，用于存储整个调用链共享的数据。
	 */
	private static final ThreadLocal<Map<String, Object>> GLOBAL_VARIABLE_MAP = new ThreadLocal<>();

	/**
	 * <p>
	 * 推入新的本地变量映射。
	 * </p>
	 * 在每层方法调用开始时调用，用于存储当前方法的本地变量。
	 */
	static void pushLocalVariableMap() {
		if (LOCAL_VARIABLE_MAP_STACK.get() == null) {
			LOCAL_VARIABLE_MAP_STACK.set(new ArrayDeque<>());
		}
		LOCAL_VARIABLE_MAP_STACK.get().push(new HashMap<>());
	}

	/**
	 * 添加本地变量。
	 * @param key 键
	 * @param value 值
	 */
	public static void putLocalVariable(String key, Object value) {
		Deque<Map<String, Object>> stack = LOCAL_VARIABLE_MAP_STACK.get();
		Assert.isTrue(stack != null && !stack.isEmpty(), "请在 [putLocalVariable] 方法调用前调用 [pushLocalVariableMap] 方法");
		stack.peek().put(key, value);
	}

	/**
	 * 获取本地变量。
	 * @param key 键
	 * @return 值
	 */
	public static Object getLocalVariable(String key) {
		Deque<Map<String, Object>> stack = LOCAL_VARIABLE_MAP_STACK.get();
		return Optional.ofNullable(stack).map(Deque::peek).map(x -> x.get(key)).orElse(null);
	}

	/**
	 * 添加全局变量。
	 * @param key 键
	 * @param value 值
	 */
	public static void putGlobalVariable(String key, Object value) {
		if (GLOBAL_VARIABLE_MAP.get() == null) {
			GLOBAL_VARIABLE_MAP.set(new HashMap<>());
		}
		GLOBAL_VARIABLE_MAP.get().put(key, value);
	}

	/**
	 * 获取全局变量。
	 * @param key 键
	 * @return 值
	 */
	public static Object getGlobalVariable(String key) {
		Map<String, Object> globalVariableMap = GLOBAL_VARIABLE_MAP.get();
		return globalVariableMap == null ? null : globalVariableMap.get(key);
	}

	/**
	 * <p>
	 * 获取变量。
	 * </p>
	 * 优先从本地变量中获取，如果没有再从全局变量中获取。
	 * @param key 键
	 * @return 值
	 */
	public static Object getVariable(String key) {
		// 先尝试从本地上下文获取
		Object localValue = getLocalVariable(key);
		if (localValue != null) {
			return localValue;
		}
		// 如果本地上下文中没有，再从全局上下文获取
		return getGlobalVariable(key);
	}

	/**
	 * <p>
	 * 获取本地变量映射。
	 * </p>
	 * @return 本地变量映射
	 */
	public static Map<String, Object> getLocalVariables() {
		Deque<Map<String, Object>> stack = LOCAL_VARIABLE_MAP_STACK.get();
		return Optional.ofNullable(stack).map(Deque::peek).orElse(new HashMap<>());
	}

	/**
	 * <p>
	 * 获取全局变量映射。
	 * </p>
	 * @return 全局变量映射
	 */
	public static Map<String, Object> getGlobalVariables() {
		return Optional.ofNullable(GLOBAL_VARIABLE_MAP.get()).orElse(new HashMap<>());
	}

	/**
	 * <p>
	 * 弹出本地变量映射。在方法调用结束时调用。
	 * </p>
	 * 如果本地上下文已经全部弹出，说明到达最外层，此时需要清理ThreadLocal
	 * @see #pushLocalVariableMap
	 */
	static void popLocalVariableMap() {
		Deque<Map<String, Object>> stack = LOCAL_VARIABLE_MAP_STACK.get();
		if (!stack.isEmpty()) {
			stack.pop();
		}
		// 如果本地变量已经全部弹出，说明到达最外层，此时需要清理ThreadLocal
		if (stack.isEmpty()) {
			OperationLogContextHolder.clear();
		}
	}

	/**
	 * 清理 ThreadLocal。
	 */
	private static void clear() {
		LOCAL_VARIABLE_MAP_STACK.remove();
		GLOBAL_VARIABLE_MAP.remove();
	}

}
