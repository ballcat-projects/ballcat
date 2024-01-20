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

package org.ballcat.common.core.strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略工厂，通过该工厂可以获取所有的策略实例。 使用案例如下:<pre>{@code
 *
 * &#64;StrategyInterface // 标记这个接口是一个策略接口
 * public interface StrategyDemo {
 *
 *     void doSomething();
 *
 * }
 *
 * &#64;StrategyComponent(strategy1) //标记这个类是一个策略的实现类，被标记的类会被注册到Spring容器中
 * public class StrategyDemoImpl1 implements StrategyDemo {
 *
 *      public void doSomething(){
 *          System.out.println("this is a strategy");
 *      }
 *
 * }
 *
 * //从工厂中获取策略的实例
 * StrategyDemo strategyDemo = StrategyFactory.getStrategy(StrategyDemo.class,"strategy1");
 * strategyDemo.doSomething();
 *
 *
 * }</pre>
 *
 * @author kevin
 * @see StrategyInterface
 * @see StrategyComponent
 */
public final class StrategyFactory {

	private StrategyFactory() {
	}

	/**
	 * 所有的策略缓存
	 */
	private static final Map<Class<?>, Map<String, Object>> STRATEGY_CONTAINER = new HashMap<>(16);

	/**
	 * 向策略工厂中添加一个策略
	 * @param strategyType 策略类的类型
	 * @param name 策略的名称
	 * @param bean 该策略类型的实例
	 */
	static void addStrategy(Class<?> strategyType, String name, Object bean) {
		synchronized (STRATEGY_CONTAINER) {
			STRATEGY_CONTAINER.computeIfAbsent(strategyType, k -> new ConcurrentHashMap<>(8)).put(name, bean);
		}
	}

	/**
	 * 从工厂中获取一个策略
	 * @param strategyType 策略类的类型
	 * @param name 策略的名称
	 * @return 策略的实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> Optional<T> getStrategy(Class<T> strategyType, String name) {
		return Optional.ofNullable(STRATEGY_CONTAINER.get(strategyType)).map(strategyMap -> (T) strategyMap.get(name));
	}

	/**
	 * 判断工厂中是否存在某种策略
	 * @param strategyType 策略类的类型
	 * @param name 策略的名称
	 * @return 如果策略存在，则返回true，否则返回false
	 */
	public static boolean exists(Class<?> strategyType, String name) {
		return Optional.ofNullable(STRATEGY_CONTAINER.get(strategyType))
			.map(strategyMap -> strategyMap.containsKey(name))
			.orElse(Boolean.FALSE);
	}

	/**
	 * 从工厂中获取非空的策略，如果为空则抛出异常
	 * @param strategyType 策略的类型
	 * @param name 策略的名称
	 * @return 策略的实例
	 */
	public static <T> T getNonNullStrategy(Class<T> strategyType, String name) {
		return getStrategy(strategyType, name).orElseThrow(() -> new NoSuchStrategyException(strategyType, name));
	}

}
