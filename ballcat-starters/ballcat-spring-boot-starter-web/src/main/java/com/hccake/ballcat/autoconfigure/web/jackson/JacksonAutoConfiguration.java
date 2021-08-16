package com.hccake.ballcat.autoconfigure.web.jackson;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hccake.ballcat.common.core.jackson.JavaTimeModule;
import com.hccake.ballcat.common.core.jackson.NullSerializerModifier;
import com.hccake.ballcat.common.desensitize.json.JsonSerializerModifier;
import com.hccake.ballcat.common.util.json.JacksonJsonToolAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 22:14
 */
@Configuration
public class JacksonAutoConfiguration {

	/**
	 * 自定义objectMapper
	 * @return ObjectMapper
	 */
	@Bean
	@ConditionalOnClass(ObjectMapper.class)
	@ConditionalOnMissingBean(ObjectMapper.class)
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		// org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperConfiguration
		ObjectMapper objectMapper = builder.createXmlMapper(false).build();

		// 对于空对象的序列化不抛异常
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 序列化时忽略未知属性
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// NULL值修改
		objectMapper.setSerializerFactory(
				objectMapper.getSerializerFactory().withSerializerModifier(new NullSerializerModifier()));
		// 时间解析器
		objectMapper.registerModule(new JavaTimeModule());
		// 有特殊需要转义字符, 不报错
		objectMapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());

		// 更新 JsonUtils 中的 ObjectMapper，保持容器和工具类中的 ObjectMapper 对象一致
		JacksonJsonToolAdapter.setMapper(objectMapper);

		return objectMapper;
	}

	/**
	 * 注册 Jackson 的脱敏序列化器
	 * @return Jackson2ObjectMapperBuilderCustomizer
	 */
	@Bean
	@ConditionalOnMissingBean(name = "desensitizeCustomizer")
	public Jackson2ObjectMapperBuilderCustomizer desensitizeCustomizer() {
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.setSerializerModifier(new JsonSerializerModifier());
		return builder -> builder.modules(simpleModule);
	}

}
