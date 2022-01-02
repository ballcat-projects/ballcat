package com.hccake.ballcat.common.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.util.function.Consumer;
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
	 *
	 * @author lingting 2021-02-26 10:29
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
