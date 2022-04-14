package com.hccake.ballcat.extend.openapi;

import cn.hutool.core.collection.ListUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;
import java.util.Map;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/11/1 19:37
 */
@Data
@ConfigurationProperties(OpenApiProperties.PREFIX)
public class OpenApiProperties {

	public static final String PREFIX = "ballcat.openapi";

	/**
	 * 是否开启 openApi 文档
	 */
	private Boolean enabled = true;

	/**
	 * 文档基本信息
	 */
	@NestedConfigurationProperty
	private InfoProperties info = new InfoProperties();

	/**
	 * 扩展文档地址
	 */
	@NestedConfigurationProperty
	private ExternalDocumentation externalDocs;

	/**
	 * Api 服务
	 * @see <a href="https://swagger.io/docs/specification/api-host-and-base-path/">API
	 * Server and Base URL</a>
	 */
	private List<Server> servers = null;

	/**
	 * 安全配置
	 * @see <a href="https://swagger.io/docs/specification/authentication/">Authentication
	 */
	private List<SecurityRequirement> security = null;

	/**
	 * 标签
	 */
	private List<Tag> tags = null;

	/**
	 * 路径
	 */
	@NestedConfigurationProperty
	private Paths paths = null;

	/**
	 * 组件
	 */
	@NestedConfigurationProperty
	private Components components = null;

	/**
	 * 扩展信息
	 *
	 * map 没有提示：https://github.com/spring-projects/spring-boot/issues/9945
	 */
	private Map<String, Object> extensions = null;

	/**
	 * 跨域配置
	 */
	private CorsConfig corsConfig;

	/**
	 * <p>
	 * 文档的基础属性信息
	 * </p>
	 *
	 * @see io.swagger.v3.oas.models.info.Info
	 *
	 * 为了 springboot 自动生产配置提示信息，所以这里复制一个类出来
	 */
	@Data
	public static class InfoProperties {

		/**
		 * 标题
		 */
		private String title = null;

		/**
		 * 描述
		 */
		private String description = null;

		/**
		 * 服务条款URL
		 */
		private String termsOfService = null;

		/**
		 * 联系人信息
		 */
		@NestedConfigurationProperty
		private Contact contact = null;

		/**
		 * 许可证
		 */
		@NestedConfigurationProperty
		private License license = null;

		/**
		 * 版本
		 */
		private String version = null;

		/**
		 * 扩展属性
		 */
		private Map<String, Object> extensions = null;

	}

	/**
	 * <p>
	 * 跨域配置，用于文档聚合.
	 * </p>
	 *
	 * @see CorsConfiguration
	 */
	@Data
	public static class CorsConfig {

		/**
		 * 开启 Cors 跨域配置
		 */
		private boolean enabled = false;

		/**
		 * 跨域对应的 url 匹配规则
		 */
		private String urlPattern = "/**";

		/**
		 * 允许跨域的源
		 */
		private List<String> allowedOrigins;

		/**
		 * 允许跨域来源的匹配规则
		 */
		private List<String> allowedOriginPatterns;

		/**
		 * 允许跨域的方法列表
		 */
		private List<String> allowedMethods = ListUtil.toList(CorsConfiguration.ALL);

		/**
		 * 允许跨域的头信息
		 */
		private List<String> allowedHeaders = ListUtil.toList(CorsConfiguration.ALL);

		/**
		 * 额外允许跨域请求方获取的 response header 信息
		 */
		private List<String> exposedHeaders = ListUtil.toList("traceId");

		/**
		 * 是否允许跨域发送 Cookie
		 */
		private Boolean allowCredentials = true;

		/**
		 * CORS 配置缓存时间，用于控制浏览器端是否发起 Option 预检请求。 若配置此参数，在第一次获取到 CORS
		 * 的配置信息后，在过期时间内，浏览器将直接发出请求，跳过 option 预检
		 */
		private Long maxAge;

	}

}
