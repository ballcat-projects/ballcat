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

package org.ballcat.common.util;

import java.lang.reflect.Type;

import lombok.Getter;
import org.ballcat.common.util.json.FastjsonJsonToolAdapter;
import org.ballcat.common.util.json.GsonJsonToolAdapter;
import org.ballcat.common.util.json.HuToolJsonToolAdapter;
import org.ballcat.common.util.json.JacksonJsonToolAdapter;
import org.ballcat.common.util.json.JsonTool;
import org.ballcat.common.util.json.TypeReference;

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
	 */
	public static void switchAdapter(JsonTool jsonTool) {
		JsonUtils.jsonTool = jsonTool;
	}

	public static String toJson(Object obj) {
		return jsonTool.toJson(obj);
	}

	public static <T> T toObj(String json, Class<T> r) {
		return jsonTool.toObj(json, r);
	}

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

	public static <T> T toObj(String json, TypeReference<T> t) {
		return jsonTool.toObj(json, t);
	}

	private static boolean classIsPresent(String className) {
		return ClassUtils.isPresent(className, JsonUtils.class.getClassLoader());
	}

}
