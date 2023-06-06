/*
 * Copyright 2023 the original author or authors.
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
package com.hccake.ballcat.common.swagger;

import com.hccake.ballcat.common.swagger.property.SwaggerAggregatorProperties;
import com.hccake.ballcat.common.swagger.property.SwaggerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/11/1 20:03
 */
@Import(SwaggerConfiguration.class)
@ConditionalOnProperty(prefix = SwaggerProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerAggregatorAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SwaggerAggregatorProperties swaggerAggregatorProperties() {
		return new SwaggerAggregatorProperties();
	}

	/**
	 * 聚合文档
	 * @param defaultResourcesProvider 本地内存的资源提供者
	 * @return SwaggerResourcesProvider
	 */
	@Primary
	@Bean
	@ConditionalOnBean(SwaggerAggregatorProperties.class)
	public SwaggerResourcesProvider swaggerResourcesProvider(InMemorySwaggerResourcesProvider defaultResourcesProvider,
			SwaggerAggregatorProperties swaggerAggregatorProperties) {

		return () -> {
			// 聚合者自己的 Resources
			List<SwaggerResource> resources = new ArrayList<>(defaultResourcesProvider.get());
			// 提供者的 Resources
			List<SwaggerResource> providerResources = swaggerAggregatorProperties.getProviderResources();
			if (!CollectionUtils.isEmpty(providerResources)) {
				resources.addAll(providerResources);
			}
			return resources;
		};
	}

}
