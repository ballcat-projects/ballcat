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

package org.ballcat.autoconfigure.web.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.ballcat.common.core.jackson.CustomJavaTimeModule;
import org.ballcat.common.core.jackson.NullSerializerProvider;
import org.ballcat.common.util.json.JacksonJsonToolAdapter;
import org.ballcat.desensitize.json.DesensitizeStrategy;
import org.ballcat.desensitize.json.JsonDesensitizeModule;
import org.ballcat.desensitize.json.JsonDesensitizeSerializerModifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Hccake 2019/10/17 22:14
 */
@RequiredArgsConstructor
@AutoConfiguration(before = JacksonAutoConfiguration.class)
@EnableConfigurationProperties({ JacksonProperties.class })
public class CustomJacksonAutoConfiguration {

	private final JacksonProperties jacksonProperties;

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> {
			if (null != this.jacksonProperties.getSerialization()) {
				JacksonProperties.Serialization serialization = this.jacksonProperties.getSerialization();
				final NullSerializerProvider nullSerializerProvider = new NullSerializerProvider();
				nullSerializerProvider
					.setWriteNullStringValuesAsQuotes(serialization.isWriteNullStringValuesAsQuotes());
				nullSerializerProvider.setWriteNullMapValuesAsBraces(serialization.isWriteNullMapValuesAsBraces());
				nullSerializerProvider
					.setWriteNullArrayValuesAsBrackets(serialization.isWriteNullArrayValuesAsBrackets());
				builder.postConfigurer(c -> c.setSerializerProvider(nullSerializerProvider));
			}
		};
	}

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
		// 更新 JsonUtils 中的 ObjectMapper，保持容器和工具类中的 ObjectMapper 对象一致
		JacksonJsonToolAdapter.setMapper(objectMapper);
		return objectMapper;
	}

	/**
	 * 注册自定义 的 jackson 时间格式，高优先级，用于覆盖默认的时间格式
	 * @return CustomJavaTimeModule
	 */
	@Bean
	@ConditionalOnMissingBean(CustomJavaTimeModule.class)
	public CustomJavaTimeModule customJavaTimeModule() {
		return new CustomJavaTimeModule();
	}

	/**
	 * 注册 Jackson 的脱敏模块
	 * @return Jackson2ObjectMapperBuilderCustomizer
	 */
	@Bean
	@ConditionalOnMissingBean({ JsonDesensitizeModule.class, DesensitizeStrategy.class })
	public JsonDesensitizeModule jsonDesensitizeModule() {
		JsonDesensitizeSerializerModifier desensitizeModifier = new JsonDesensitizeSerializerModifier();
		return new JsonDesensitizeModule(desensitizeModifier);
	}

	/**
	 * 注册 Jackson 的脱敏模块
	 * @return Jackson2ObjectMapperBuilderCustomizer
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(DesensitizeStrategy.class)
	public JsonDesensitizeModule jsonDesensitizeModule(DesensitizeStrategy desensitizeStrategy) {
		JsonDesensitizeSerializerModifier desensitizeModifier = new JsonDesensitizeSerializerModifier(
				desensitizeStrategy);
		return new JsonDesensitizeModule(desensitizeModifier);
	}

}
