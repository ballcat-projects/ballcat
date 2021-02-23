package com.hccake.ballcat.common.desensitize;

import com.hccake.ballcat.common.desensitize.handler.SimpleDesensitizationHandler;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 脱敏处理器持有者，使用SPI方式加载所有的脱敏处理器，便于用户扩展处理
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public class SimpleDesensitizationHandlerHolder {

	private final static Map<Class<? extends SimpleDesensitizationHandler>, SimpleDesensitizationHandler> MAP = new ConcurrentHashMap<>();

	static {
		// SPI 加载所有的脱敏类型处理
		ServiceLoader<SimpleDesensitizationHandler> loadedDrivers = ServiceLoader
				.load(SimpleDesensitizationHandler.class);
		for (SimpleDesensitizationHandler desensitizationHandler : loadedDrivers) {
			MAP.put(desensitizationHandler.getClass(), desensitizationHandler);
		}
	}

	/**
	 * 获取Handler
	 * @param handlerClass SimpleDesensitizationHandler的实现类
	 * @return 处理器实例
	 */
	public static SimpleDesensitizationHandler getHandler(Class<? extends SimpleDesensitizationHandler> handlerClass) {
		return MAP.get(handlerClass);
	}

	/**
	 * 添加Handler
	 * @param handlerClass SimpleDesensitizationHandler的实现类
	 * @param handler 处理器实例
	 * @return handler 处理器实例
	 */
	public static SimpleDesensitizationHandler addHandler(Class<? extends SimpleDesensitizationHandler> handlerClass,
			SimpleDesensitizationHandler handler) {
		return MAP.put(handlerClass, handler);
	}

}
