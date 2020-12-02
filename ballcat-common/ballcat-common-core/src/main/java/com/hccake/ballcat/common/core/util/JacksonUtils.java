package com.hccake.ballcat.common.core.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.SneakyThrows;

/**
 * @author lingting 2020-12-02 20:06
 */
public class JacksonUtils {

	@Getter
	static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	/**
	 * 对 mapper 进行配置
	 * @author lingting 2020-12-02 20:24
	 */
	public static void config(Consumer<ObjectMapper> consumer) {
		consumer.accept(mapper);
	}

	/**
	 * 对象转 json string
	 * @author lingting 2020-12-02 20:08
	 */
	@SneakyThrows
	public static String toJson(Object o) {
		return mapper.writeValueAsString(o);
	}

	/**
	 * @param json json string
	 * @param <T> 目标类型
	 * @author lingting 2020-12-02 20:12
	 */
	@SneakyThrows
	public static <T> T toObj(String json, Class<T> t) {
		return mapper.readValue(json, t);
	}

	/**
	 * @param json json string
	 * @param type 目标类型
	 * @author lingting 2020-12-02 20:12
	 */
	@SneakyThrows
	public static <T> T toObj(String json, Type type) {
		return mapper.readValue(json, mapper.constructType(type));
	}

	/**
	 * @param json json string
	 * @param ref 目标类型
	 * @author lingting 2020-12-02 20:12
	 */
	@SneakyThrows
	public static <T> T toObj(String json, TypeReference<T> ref) {
		return mapper.readValue(json, ref);
	}

	/**
	 * @param inputStream json string 输入流
	 * @param <T> 目标类型
	 * @author lingting 2020-12-02 20:12
	 */
	@SneakyThrows
	public static <T> T toObj(InputStream inputStream, Class<T> t) {
		return mapper.readValue(inputStream, t);
	}

	/**
	 * @param inputStream json string 输入流
	 * @param type 目标类型
	 * @author lingting 2020-12-02 20:12
	 */
	@SneakyThrows
	public static <T> T toObj(InputStream inputStream, Type type) {
		return mapper.readValue(inputStream, mapper.constructType(type));
	}

	/**
	 * @param inputStream json string 输入流
	 * @param ref 目标类型
	 * @author lingting 2020-12-02 20:12
	 */
	@SneakyThrows
	public static <T> T toObj(InputStream inputStream, TypeReference<T> ref) {
		return mapper.readValue(inputStream, ref);
	}

	/**
	 * @param json json string
	 * @return {@link JsonNode}
	 */
	@SneakyThrows
	public static JsonNode toJsonNode(String json) {
		return mapper.readTree(json);
	}

	/**
	 * @author lingting 2020-12-02 20:18
	 */
	public static void registerSubtype(Class<?> clz, String type) {
		mapper.registerSubtypes(new NamedType(clz, type));
	}

	/**
	 * @author lingting 2020-12-02 20:18
	 */
	public static ObjectNode createEmptyJsonNode() {
		return new ObjectNode(mapper.getNodeFactory());
	}

	/**
	 * @author lingting 2020-12-02 20:18
	 */
	public static ArrayNode createEmptyArrayNode() {
		return new ArrayNode(mapper.getNodeFactory());
	}

	/**
	 * @author lingting 2020-12-02 20:18
	 */
	public static JsonNode transferToJsonNode(Object obj) {
		return mapper.valueToTree(obj);
	}

	/**
	 * @author lingting 2020-12-02 20:18
	 */
	public static JavaType constructJavaType(Type type) {
		return mapper.constructType(type);
	}

}
