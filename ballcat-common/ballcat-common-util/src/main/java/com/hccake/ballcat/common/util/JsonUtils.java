package com.hccake.ballcat.common.util;

import com.hccake.ballcat.common.util.json.*;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Type;

/**
 * @author lingting 2021/2/25 20:38
 */
public final class JsonUtils {

	private JsonUtils() {
	}

	@Getter
	private static JsonTool jsonTool;

	static {
		ClassLoader classLoader = JsonUtils.class.getClassLoader();
		if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader)) {
			jsonTool = new JacksonJsonToolAdapter();
		}
		else if (ClassUtils.isPresent("com.google.gson.Gson", classLoader)) {
			jsonTool = new GsonJsonToolAdapter();
		}
		else if (ClassUtils.isPresent("cn.hutool.json.JSONConfig", classLoader)) {
			jsonTool = new HuToolJsonToolAdapter();
		}
		else if (ClassUtils.isPresent("com.alibaba.fastjson.JSON", classLoader)) {
			jsonTool = new FastjsonJsonToolAdapter();
		}
	}

	/**
	 * 切换适配器. 请注意 本方法全局生效
	 *
	 * @author lingting 2021-02-26 11:18
	 */
	public static void switchAdapter(JsonTool jsonTool) {
		JsonUtils.jsonTool = jsonTool;
	}

	@SneakyThrows
	public static String toJson(Object obj) {
		return jsonTool.toJson(obj);
	}

	@SneakyThrows
	public static <T> T toObj(String json, Class<T> r) {
		return jsonTool.toObj(json, r);
	}

	@SneakyThrows
	public static <T> T toObj(String json, Type t) {
		return jsonTool.toObj(json, t);
	}

	@SneakyThrows
	public static <T> T toObj(String json, TypeReference<T> t) {
		return jsonTool.toObj(json, t);
	}

}
