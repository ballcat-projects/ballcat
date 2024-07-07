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

package org.ballcat.autoconfigure.openapi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.ballcat.autoconfigure.web.pageable.PageableProperties;
import org.ballcat.common.model.domain.PageParam;
import org.ballcat.common.model.domain.PageableConstants;
import org.ballcat.openapi.pageable.PageParamOpenAPIConverter;
import org.ballcat.openapi.pageable.PageableDelegatingMethodParameterCustomizer;
import org.ballcat.openapi.pageable.PageableRequestClassCreator;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * OpenAPI 的自动配置类
 *
 * @author hccake
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(OpenApiProperties.class)
@AutoConfigureBefore(SpringDocConfiguration.class)
@ConditionalOnProperty(prefix = OpenApiProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class OpenApiAutoConfiguration {

	private final OpenApiProperties openApiProperties;

	@Bean
	@ConditionalOnMissingBean(OpenAPI.class)
	@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
	public OpenAPI openAPI() {

		OpenAPI openAPI = new OpenAPI();

		// 文档基本信息
		OpenApiProperties.InfoProperties infoProperties = this.openApiProperties.getInfo();
		Info info = convertInfo(infoProperties);
		openAPI.info(info);

		// 扩展文档信息
		openAPI.externalDocs(this.openApiProperties.getExternalDocs());
		openAPI.servers(this.openApiProperties.getServers());
		openAPI.security(this.openApiProperties.getSecurity());
		openAPI.tags(this.openApiProperties.getTags());
		openAPI.paths(this.openApiProperties.getPaths());
		openAPI.components(this.openApiProperties.getComponents());
		openAPI.extensions(this.openApiProperties.getExtensions());

		return openAPI;
	}

	@SuppressWarnings("java:S1854")
	private Info convertInfo(OpenApiProperties.InfoProperties infoProperties) {
		Info info = new Info();
		info.setTitle(infoProperties.getTitle());
		info.setDescription(infoProperties.getDescription());
		info.setTermsOfService(infoProperties.getTermsOfService());
		info.setContact(infoProperties.getContact());
		info.setLicense(infoProperties.getLicense());
		info.setVersion(infoProperties.getVersion());
		info.setExtensions(infoProperties.getExtensions());
		return info;
	}

	/**
	 * The type Spring doc pageParam configuration.
	 */
	@RequiredArgsConstructor
	@ConditionalOnClass(PageableProperties.class)
	static class SpringDocPageParamConfiguration {

		private final PageableProperties pageableProperties;

		/**
		 * PageParam open api converter pageable open api converter.
		 * @param objectMapperProvider the object mapper provider
		 * @return the pageParam open api converter
		 */
		@Bean
		@ConditionalOnMissingBean
		PageParamOpenAPIConverter pageParamAPIConverter(ObjectMapperProvider objectMapperProvider) throws IOException {

			Map<String, String> map = new HashMap<>();

			String page = this.pageableProperties.getPageParameterName();
			if (!PageableConstants.DEFAULT_PAGE_PARAMETER.equals(page)) {
				map.put(PageableConstants.DEFAULT_PAGE_PARAMETER, page);
			}

			String size = this.pageableProperties.getSizeParameterName();
			if (!PageableConstants.DEFAULT_SIZE_PARAMETER.equals(size)) {
				map.put(PageableConstants.DEFAULT_SIZE_PARAMETER, size);
			}

			String sort = this.pageableProperties.getSortParameterName();
			if (!PageableConstants.DEFAULT_SORT_PARAMETER.equals(sort)) {
				map.put(PageableConstants.DEFAULT_SORT_PARAMETER, sort);
			}

			// 由于 PageParam 是由自定义的 PageParamArgumentResolver 处理的，所以需要在文档上进行入参的格式转换
			SpringDocUtils config = SpringDocUtils.getConfig();
			Class<?> pageParamRequestClass = PageableRequestClassCreator.create(map);
			config.replaceParameterObjectWithClass(PageParam.class, pageParamRequestClass);
			return new PageParamOpenAPIConverter(objectMapperProvider);
		}

		/**
		 * 支持 #{@link org.ballcat.web.pageable.Pageable} 注解的参数自定义。
		 * @return PageableDelegatingMethodParameterCustomizer
		 */
		@Bean
		@ConditionalOnMissingBean
		PageableDelegatingMethodParameterCustomizer pageableDelegatingMethodParameterCustomizer() {
			return new PageableDelegatingMethodParameterCustomizer();
		}

	}

}
