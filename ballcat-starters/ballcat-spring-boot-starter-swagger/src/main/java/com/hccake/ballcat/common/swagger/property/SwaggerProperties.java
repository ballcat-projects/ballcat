package com.hccake.ballcat.common.swagger.property;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/11/1 19:37
 */
@Data
@ConfigurationProperties("swagger")
public class SwaggerProperties {

	/**
	 * 是否开启swagger
	 */
	private Boolean enabled;

	/**
	 * 分组名称
	 */
	private String groupName;

	/**
	 * 额外的Model的包路径 用于扫描并加载未被 swagger 引入的 model， 例如未明确在接口定义会返回的类，或者一些公共的类
	 **/
	private String[] additionalModelPackage = {};

	/**
	 * 标题
	 **/
	private String title = "";

	/**
	 * 描述
	 **/
	private String description = "";

	/**
	 * 版本
	 **/
	private String version = "";

	/**
	 * 许可证
	 **/
	private String license = "";

	/**
	 * 许可证URL
	 **/
	private String licenseUrl = "";

	/**
	 * 服务条款URL
	 **/
	private String termsOfServiceUrl = "";

	/**
	 * host信息
	 **/
	private String host = "";

	/**
	 * 联系人信息
	 */
	private Contact contact = new Contact();

	/**
	 * 全局统一鉴权配置
	 **/
	private Authorization authorization = new Authorization();

	@Data
	@NoArgsConstructor
	public static class Contact {

		/**
		 * 联系人
		 **/
		private String name = "";

		/**
		 * 联系人url
		 **/
		private String url = "";

		/**
		 * 联系人email
		 **/
		private String email = "";

	}

	@Data
	@NoArgsConstructor
	public static class Authorization {

		/**
		 * 鉴权策略ID，需要和SecurityReferences ID保持一致
		 */
		private String name = "";

		/**
		 * 需要开启鉴权URL的正则
		 */
		private String authRegex = "^.*$";

		/**
		 * 鉴权作用域列表
		 */
		private List<AuthorizationScope> authorizationScopeList = new ArrayList<>();

		private List<String> tokenUrlList = new ArrayList<>();

	}

	@Data
	@NoArgsConstructor
	public static class AuthorizationScope {

		/**
		 * 作用域名称
		 */
		private String scope = "";

		/**
		 * 作用域描述
		 */
		private String description = "";

	}

}
