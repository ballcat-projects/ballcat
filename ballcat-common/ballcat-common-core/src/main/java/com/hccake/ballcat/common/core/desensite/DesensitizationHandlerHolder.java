package com.hccake.ballcat.common.core.desensite;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 脱敏处理器持有者，使用SPI方式加载所有的脱敏处理器，便于用户扩展处理
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public class DesensitizationHandlerHolder {

	public final static Map<String, DesensitizationHandler> TYPE_MAPS = new ConcurrentHashMap<>();

	static {
		// SPI 加载所有的脱敏类型处理
		ServiceLoader<DesensitizationHandler> loadedDrivers = ServiceLoader.load(DesensitizationHandler.class);
		for (DesensitizationHandler desensitizationHandler : loadedDrivers) {
			TYPE_MAPS.put(desensitizationHandler.getType(), desensitizationHandler);
		}
	}

}
