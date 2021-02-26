package com.hccake.ballcat.common.util.json;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.function.Consumer;

/**
 * @author lingting 2021/2/26 10:00
 */
public class HuToolJsonToolAdapter implements JsonTool {

	@Getter
	static JSONConfig jsonConfig = JSONConfig.create();

	static {

	}

	public void config(Consumer<JSONConfig> consumer) {
		consumer.accept(jsonConfig);
	}

	@Override
	public String toJson(Object obj) {
		return JSONUtil.parse(obj, jsonConfig).toJSONString(0);
	}

	@Override
	public <T> T toObj(String json, Class<T> r) {
		return JSONUtil.parse(json, jsonConfig).toBean(r);
	}

	@Override
	public <T> T toObj(String json, Type t) {
		return JSONUtil.parse(json, jsonConfig).toBean(t);
	}

	@Override
	public <T> T toObj(String json, TypeReference<T> t) {
		return JSONUtil.parse(json, jsonConfig).toBean(new cn.hutool.core.lang.TypeReference<T>() {
			@Override
			public Type getType() {
				return t.getType();
			}
		});
	}

}
