package com.hccake.ballcat.common.desensitize;

import com.hccake.ballcat.common.desensitize.handler.DesensitizationHandler;
import com.hccake.ballcat.common.desensitize.handler.RegexDesensitizationHandler;
import com.hccake.ballcat.common.desensitize.handler.SimpleDesensitizationHandler;
import com.hccake.ballcat.common.desensitize.handler.SlideDesensitizationHandler;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 脱敏处理器持有者
 * <p>
 * - 默认提供 Regex 和 Slide 类型的脱敏处理器 <br/>
 * - Simple 脱敏处理器则使用SPI方式加载，便于用户扩展处理
 * </p>
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public final class DesensitizationHandlerHolder {

	private DesensitizationHandlerHolder() {
	}

	private static final Map<Class<? extends DesensitizationHandler>, DesensitizationHandler> MAP = new ConcurrentHashMap<>();

	static {
		// 滑动脱敏处理器
		MAP.put(SlideDesensitizationHandler.class, new SlideDesensitizationHandler());
		// 正则脱敏处理器
		MAP.put(RegexDesensitizationHandler.class, new RegexDesensitizationHandler());
		// SPI 加载所有的 Simple脱敏类型处理
		ServiceLoader<SimpleDesensitizationHandler> loadedDrivers = ServiceLoader
				.load(SimpleDesensitizationHandler.class);
		for (SimpleDesensitizationHandler desensitizationHandler : loadedDrivers) {
			MAP.put(desensitizationHandler.getClass(), desensitizationHandler);
		}
	}

	/**
	 * 获取 DesensitizationHandler
	 * @return 处理器实例
	 */
	public static DesensitizationHandler getHandler(Class<? extends DesensitizationHandler> handlerClass) {
		return MAP.get(handlerClass);
	}

	/**
	 * 获取 RegexDesensitizationHandler
	 * @return 处理器实例
	 */
	public static RegexDesensitizationHandler getRegexDesensitizationHandler() {
		return (RegexDesensitizationHandler) MAP.get(RegexDesensitizationHandler.class);
	}

	/**
	 * 获取 SlideDesensitizationHandler
	 * @return 处理器实例
	 */
	public static SlideDesensitizationHandler getSlideDesensitizationHandler() {
		return (SlideDesensitizationHandler) MAP.get(SlideDesensitizationHandler.class);
	}

	/**
	 * 获取指定的 SimpleDesensitizationHandler
	 * @param handlerClass SimpleDesensitizationHandler的实现类
	 * @return 处理器实例
	 */
	public static SimpleDesensitizationHandler getSimpleHandler(
			Class<? extends SimpleDesensitizationHandler> handlerClass) {
		return (SimpleDesensitizationHandler) MAP.get(handlerClass);
	}

	/**
	 * 添加Handler
	 * @param handlerClass DesensitizationHandler的实现类
	 * @param handler 处理器实例
	 * @return handler 处理器实例
	 */
	public static DesensitizationHandler addHandler(Class<? extends DesensitizationHandler> handlerClass,
			DesensitizationHandler handler) {
		return MAP.put(handlerClass, handler);
	}

}
