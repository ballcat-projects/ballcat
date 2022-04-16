package com.ballcat.core.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.common.core.jackson.NullSerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author hccake
 */
@Slf4j
class NullSerializerTest {

	@Test
	void test() throws JsonProcessingException {
		// 原始输出
		ObjectMapper objectMapper = new ObjectMapper();

		String json = objectMapper.writeValueAsString(new NullSeriralDemoData());
		Assertions.assertEquals("{\"str\":null,\"map\":null,\"collection\":null}", json, "Json 序列化异常");
	}

	@Test
	void test2() throws JsonProcessingException {
		// NULL值修改
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializerProvider(new NullSerializerProvider());

		String json = objectMapper.writeValueAsString(new NullSeriralDemoData());
		Assertions.assertEquals("{\"str\":\"\",\"map\":{},\"collection\":[]}", json, "Json 序列化异常");
	}

}
