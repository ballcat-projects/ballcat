package com.hccake.ballcat.extend.openapi;

import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageParamRequest;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.SpringDocUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * OpenAPI 的自动配置类
 *
 * @author hccake
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(OpenApiProperties.class)
@ConditionalOnProperty(prefix = OpenApiProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class OpenApiAutoConfiguration {

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
		OpenApiProperties.InfoProperties infoProperties = openApiProperties.getInfo();
		Info info = convertInfo(infoProperties);
		openAPI.info(info);

		// 扩展文档信息
		openAPI.externalDocs(openApiProperties.getExternalDocs());
		openAPI.servers(openApiProperties.getServers());
		openAPI.security(openApiProperties.getSecurity());
		openAPI.tags(openApiProperties.getTags());
		openAPI.paths(openApiProperties.getPaths());
		openAPI.components(openApiProperties.getComponents());
		openAPI.extensions(openApiProperties.getExtensions());

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
		corsConfiguration.setAllowedOriginPatterns(corsConfig.getAllowedOriginPatterns());
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
