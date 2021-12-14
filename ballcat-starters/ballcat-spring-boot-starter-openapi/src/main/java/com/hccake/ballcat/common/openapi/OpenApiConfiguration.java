package com.hccake.ballcat.common.openapi;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageParamRequest;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.SpringDocUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * OpenAPI 的自动配置类
 *
 * @author hccake
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(OpenApiProperties.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = OpenApiProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class OpenApiConfiguration {

	private final OpenApiProperties openApiProperties;

	static {
		// 由于 PageParam 是由自定义的 PageParamArgumentResolver 处理的，所以需要在文档上进行入参的格式转换
		SpringDocUtils config = SpringDocUtils.getConfig();
		config.replaceParameterObjectWithClass(PageParam.class, PageParamRequest.class);
	}

	@Bean
	@ConditionalOnMissingBean(OpenAPI.class)
	@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
	public OpenAPI openAPI() {

		OpenAPI openAPI = new OpenAPI();

		// 文档基本信息
		Info info = new Info();
		info.setTitle(openApiProperties.getTitle());
		info.setDescription(openApiProperties.getDescription());
		info.setVersion(openApiProperties.getVersion());
		info.setTermsOfService(openApiProperties.getTermsOfService());
		info.setLicense(openApiProperties.getLicense());
		info.setContact(openApiProperties.getContact());
		openAPI.info(info);

		// 扩展文档信息
		openAPI.externalDocs(openApiProperties.getExternalDocs());

		// 添加文档的安全性校验支持
		Map<String, SecurityScheme> securitySchemes = openApiProperties.getSecuritySchemes();
		// see https://github.com/springdoc/springdoc-openapi/issues/696
		if (CollectionUtil.isNotEmpty(securitySchemes)) {
			Components components = new Components();
			for (Map.Entry<String, SecurityScheme> infoEntry : securitySchemes.entrySet()) {
				String key = infoEntry.getKey();
				SecurityScheme securityScheme = infoEntry.getValue();
				components.addSecuritySchemes(key, securityScheme);
			}
			openAPI.components(components);
		}

		// 添加全局的安全验证设置
		Map<String, List<String>> globalSecurityRequirements = openApiProperties.getGlobalSecurityRequirements();
		if (CollectionUtil.isNotEmpty(globalSecurityRequirements)) {
			for (Map.Entry<String, List<String>> entry : globalSecurityRequirements.entrySet()) {
				String key = entry.getKey();
				// 只有 oauth2 和 openIdConnect 类型的 SecurityScheme 才需要 Scopes
				List<String> scopes = CollectionUtil.isEmpty(entry.getValue()) ? Collections.emptyList()
						: entry.getValue();
				openAPI.addSecurityItem(new SecurityRequirement().addList(key, scopes));
			}
		}

		return openAPI;
	}

	/**
	 * 允许聚合者对提供者的文档进行跨域访问 解决聚合文档导致的跨域问题
	 * @return FilterRegistrationBean
	 */
	@Bean
	@ConditionalOnProperty(prefix = OpenApiProperties.PREFIX + ".cors-config", name = "enabled", havingValue = "true")
	public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
		// 获取 CORS 配置
		OpenApiProperties.CorsConfig corsConfig = openApiProperties.getCorsConfig();

		// 转换 CORS 配置
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOrigins(corsConfig.getAllowedOrigins());
		corsConfiguration.setAllowedOriginPatterns(corsConfig.getAllowedOrigins());
		corsConfiguration.setAllowedMethods(corsConfig.getAllowedMethods());
		corsConfiguration.setAllowedHeaders(corsConfig.getAllowedHeaders());
		corsConfiguration.setExposedHeaders(corsConfig.getExposedHeaders());
		corsConfiguration.setAllowCredentials(corsConfig.getAllowCredentials());
		corsConfiguration.setMaxAge(corsConfig.getMaxAge());

		// 注册 CORS 配置与资源的映射关系
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(corsConfig.getUrlPattern(), corsConfiguration);

		// 注册 CORS 过滤器，设置最高优先级
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

		return bean;
	}

}
