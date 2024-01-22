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
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author lingting 2021/2/26 10:32
 */
public class FastjsonJsonToolAdapter implements JsonTool {

	/**
	 * json str -> obj 时
	 */
	static final List<Feature> FEATURES = new ArrayList<>(Feature.values().length);

	/**
	 * obj -> json str
	 */
	static final List<SerializerFeature> SERIALIZER_FEATURES = new ArrayList<>(SerializerFeature.values().length);

	private static Feature[] features = new Feature[0];

	private static SerializerFeature[] serializerFeatures = new SerializerFeature[0];

	/**
	 * 不要使用 config 以外的形式更新配置
	 */
	public void config(BiConsumer<List<Feature>, List<SerializerFeature>> consumer) {
		consumer.accept(FEATURES, SERIALIZER_FEATURES);
		features = FEATURES.toArray(new Feature[0]);
		serializerFeatures = SERIALIZER_FEATURES.toArray(new SerializerFeature[0]);
	}

	@Override
	public String toJson(Object obj) {
		return JSON.toJSONString(obj, serializerFeatures);
	}

	@Override
	public <T> T toObj(String json, Class<T> r) {
		return JSON.parseObject(json, r, features);
	}

	@Override
	public <T> T toObj(String json, Type t) {
		return JSON.parseObject(json, t, features);
	}

	@Override
	public <T> T toObj(String json, TypeReference<T> t) {
		/*
		 * 由于 fastjson 下面这个方法 com.alibaba.fastjson.JSON.parseObject(java.lang.String,
		 * com.alibaba.fastjson.TypeReference<T>, com.alibaba .fastjson.parser.Feature...)
		 * 直接调用了 type.type, 而不是使用的 getType()方法. 所以使用
		 * com.debug.json.FastjsonAdapter.toObj(java.lang.String, java.lang .reflect.Type)
		 * 方法
		 */

		return toObj(json, t.getType());
	}

}
