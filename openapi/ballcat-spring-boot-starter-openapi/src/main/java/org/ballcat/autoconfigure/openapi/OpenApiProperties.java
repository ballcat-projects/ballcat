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

import java.util.List;
import java.util.Map;

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

/**
 * @author Hccake 2019/11/1 19:37
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
	 * <p>
	 * map 类型属性没有 IDE
	 * 提示，<a href="https://github.com/spring-projects/spring-boot/issues/9945">gh-9945</a>
	 */
	private Map<String, Object> extensions = null;

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

}
