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

import cn.hutool.core.text.CharSequenceUtil;
import com.hccake.ballcat.common.swagger.builder.DocketBuildHelper;
import com.hccake.ballcat.common.swagger.builder.MultiRequestHandlerSelectors;
import com.hccake.ballcat.common.swagger.property.SwaggerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/11/1 19:43
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfiguration {

	private final SwaggerProperties swaggerProperties;

	@Bean
	@ConditionalOnMissingBean
	public Docket api() {

		DocketBuildHelper helper = new DocketBuildHelper(swaggerProperties);

		// @formatter:off
		// 1. 文档信息构建
		Docket docket = new Docket(swaggerProperties.getDocumentationType().getType())
				.host(swaggerProperties.getHost())
				.apiInfo(helper.apiInfo())
				.groupName(swaggerProperties.getGroupName())
				.enable(swaggerProperties.getEnabled());

		// 2. 安全配置
		docket.securitySchemes(helper.securitySchema())
				.securityContexts(helper.securityContext());

		// 3. 文档筛选
		String basePackage = swaggerProperties.getBasePackage();
		ApiSelectorBuilder select = docket.select();
		if(CharSequenceUtil.isEmpty(basePackage)){
			select.apis(MultiRequestHandlerSelectors.any());
		}else {
			select.apis(MultiRequestHandlerSelectors.basePackage(basePackage));
		}
		select.paths(helper.paths()).build();

		return docket;
		// @formatter:on
	}

}
