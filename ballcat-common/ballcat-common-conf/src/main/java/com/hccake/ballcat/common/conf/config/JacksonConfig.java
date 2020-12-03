package com.hccake.ballcat.common.conf.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.common.core.jackson.ArraySerializerModifier;
import com.hccake.ballcat.common.core.jackson.JavaTimeModule;
import com.hccake.ballcat.common.core.jackson.NullSerializer;
import com.hccake.ballcat.common.core.util.JacksonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 22:14
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class JacksonConfig {

	/**
	 * 自定义objectMapper
	 * @return ObjectMapper
	 */
	@Bean
	@ConditionalOnMissingBean(ObjectMapper.class)
	public ObjectMapper objectMapper() {
		JacksonUtils.config(mapper -> {
			// NULL 数组转 []
			mapper.setSerializerFactory(
					mapper.getSerializerFactory().withSerializerModifier(new ArraySerializerModifier()));
			// NULL 值转 ""
			mapper.getSerializerProvider().setNullValueSerializer(new NullSerializer());
			// 时间解析器
			mapper.registerModule(new JavaTimeModule());
		});
		return JacksonUtils.getMapper();
	}

}
