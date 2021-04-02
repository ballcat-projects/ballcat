package com.hccake.ballcat.common.util;

import com.hccake.ballcat.common.util.json.FastjsonJsonToolAdapter;
import com.hccake.ballcat.common.util.json.GsonJsonToolAdapter;
import com.hccake.ballcat.common.util.json.HuToolJsonToolAdapter;
import com.hccake.ballcat.common.util.json.JacksonJsonToolAdapter;
import com.hccake.ballcat.common.util.json.JsonTool;
import com.hccake.ballcat.common.util.json.TypeReference;
import java.lang.reflect.Type;
import lombok.Getter;
import lombok.SneakyThrows;

/**
 * @author lingting 2021/2/25 20:38
 */
public final class JsonUtils {

	private static final String JACKSON_CLASS = "com.fasterxml.jackson.databind.ObjectMapper";

	private static final String GSON_CLASS = "com.google.gson.Gson";

	private static final String HUTOOL_JSON_CLASS = "cn.hutool.json.JSONConfig";

	private static final String HUTOOL_JSON_TYPE_REFERENCE_CLASS = "cn.hutool.core.lang.TypeReference";

	private static final String FAST_JSON_CLASS = "com.alibaba.fastjson.JSON";

	private JsonUtils() {
	}

	@Getter
	private static JsonTool jsonTool;

	static {
		if (classIsPresent(JACKSON_CLASS)) {
			jsonTool = new JacksonJsonToolAdapter();
		}
		else if (classIsPresent(GSON_CLASS)) {
			jsonTool = new GsonJsonToolAdapter();
		}
		else if (classIsPresent(HUTOOL_JSON_CLASS)) {
			jsonTool = new HuToolJsonToolAdapter();
		}
		else if (classIsPresent(FAST_JSON_CLASS)) {
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
		// 防止误传入其他类型的 typeReference 走这个方法然后转换出错
		if (classIsPresent(HUTOOL_JSON_TYPE_REFERENCE_CLASS) && t instanceof cn.hutool.core.lang.TypeReference) {
			return toObj(json, new TypeReference<T>() {
				@Override
				public Type getType() {
					return ((cn.hutool.core.lang.TypeReference<?>) t).getType();
				}
			});
		}
		else if (classIsPresent(FAST_JSON_CLASS) && t instanceof com.alibaba.fastjson.TypeReference) {
			return toObj(json, new TypeReference<T>() {
				@Override
				public Type getType() {
					return ((com.alibaba.fastjson.TypeReference<?>) t).getType();
				}
			});
		}
		else if (classIsPresent(JACKSON_CLASS) && t instanceof com.fasterxml.jackson.core.type.TypeReference) {
			return toObj(json, new TypeReference<T>() {
				@Override
				public Type getType() {
					return ((com.fasterxml.jackson.core.type.TypeReference<?>) t).getType();
				}
			});
		}

		return jsonTool.toObj(json, t);
	}

	@SneakyThrows
	public static <T> T toObj(String json, TypeReference<T> t) {
		return jsonTool.toObj(json, t);
	}

	private static boolean classIsPresent(String className) {
		return ClassUtils.isPresent(className, JsonUtils.class.getClassLoader());
	}

}
