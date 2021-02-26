package com.hccake.ballcat.common.conf.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.common.core.jackson.JavaTimeModule;
import com.hccake.ballcat.common.core.jackson.NullSerializerModifier;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.common.util.json.JacksonAdapter;
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
public class JsonConfig {

	/**
	 * 自定义objectMapper
	 * @return ObjectMapper
	 */
	@Bean
	@ConditionalOnClass(ObjectMapper.class)
	@ConditionalOnMissingBean(ObjectMapper.class)
	public ObjectMapper objectMapper() {
		JacksonAdapter adapter = (JacksonAdapter) JsonUtils.getAdapter();
		adapter.config(mapper -> {
			// NULL值修改
			mapper.setSerializerFactory(
					mapper.getSerializerFactory().withSerializerModifier(new NullSerializerModifier()));
			// 时间解析器
			mapper.registerModule(new JavaTimeModule());
		});
		return JacksonAdapter.getMapper();
	}

}
