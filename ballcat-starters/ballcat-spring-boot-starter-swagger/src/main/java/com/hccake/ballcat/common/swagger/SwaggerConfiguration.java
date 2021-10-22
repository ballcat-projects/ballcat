package com.hccake.ballcat.common.swagger;

import cn.hutool.core.util.StrUtil;
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
		if(StrUtil.isEmpty(basePackage)){
			select.apis(MultiRequestHandlerSelectors.any());
		}else {
			select.apis(MultiRequestHandlerSelectors.basePackage(basePackage));
		}
		select.paths(helper.paths()).build();

		return docket;
		// @formatter:on
	}

}
