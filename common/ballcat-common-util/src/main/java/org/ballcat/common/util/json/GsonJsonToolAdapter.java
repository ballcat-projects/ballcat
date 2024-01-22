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

package org.ballcat.common.util.json;

import java.lang.reflect.Type;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

/**
 * @author lingting 2021/2/26 10:22
 */
public class GsonJsonToolAdapter implements JsonTool {

	@Getter
	static Gson gson;

	static {
		// 初始化gson配置
		gson = new GsonBuilder().create();
	}

	/**
	 * 由于 gson 实例不能更新. 需要 create 之后生成新的实例. 请避免在运行中更新配置.
	 */
	public static void config(Consumer<GsonBuilder> consumer) {
		GsonBuilder builder = gson.newBuilder();
		consumer.accept(builder);
		// 更新 gson
		gson = builder.create();
	}

	@Override
	public String toJson(Object obj) {
		return gson.toJson(obj);
	}

	@Override
	public <T> T toObj(String json, Class<T> r) {
		return gson.fromJson(json, r);
	}

	@Override
	public <T> T toObj(String json, Type t) {
		return gson.fromJson(json, t);
	}

	@Override
	public <T> T toObj(String json, TypeReference<T> t) {
		return gson.fromJson(json, t.getType());
	}

}
