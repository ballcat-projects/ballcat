package com.hccake.ballcat.common.core.desensite;

import com.hccake.ballcat.common.core.desensite.handler.SimpleDesensitizationHandler;

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

	public final static Map<String, SimpleDesensitizationHandler> TYPE_MAPS = new ConcurrentHashMap<>();

	static {
		// SPI 加载所有的脱敏类型处理
		ServiceLoader<SimpleDesensitizationHandler> loadedDrivers = ServiceLoader
				.load(SimpleDesensitizationHandler.class);
		for (SimpleDesensitizationHandler desensitizationHandler : loadedDrivers) {
			TYPE_MAPS.put(desensitizationHandler.getType(), desensitizationHandler);
		}
	}

}
