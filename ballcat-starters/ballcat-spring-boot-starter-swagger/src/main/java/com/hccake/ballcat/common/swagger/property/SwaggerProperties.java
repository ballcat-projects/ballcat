package com.hccake.ballcat.common.swagger.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/11/1 19:37
 */
@Data
@ConfigurationProperties(SwaggerProperties.PREFIX)
public class SwaggerProperties {

	public static final String PREFIX = "ballcat.swagger";

	/**
	 * 是否开启swagger
	 */
	private Boolean enabled;

	/**
	 * 分组名称
	 */
	private String groupName;

	/**
	 * 文档版本，默认使用 2.0
	 */
	private DocumentationTypeEnum documentationType = DocumentationTypeEnum.SWAGGER_2;

	/**
	 * swagger会解析的包路径
	 **/
	private String basePackage = "";

	/**
	 * swagger会解析的url规则
	 **/
	private List<String> basePath = new ArrayList<>();

	/**
	 * 在basePath基础上需要排除的url规则
	 **/
	private List<String> excludePath = new ArrayList<>();

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
	public static class Authorization {

		/**
		 * 鉴权策略ID，需要和SecurityReferences ID保持一致
		 */
		private String name = "";

		/**
		 * 鉴权作用域列表
		 */
		private List<AuthorizationScope> authorizationScopeList = new ArrayList<>();

		/**
		 * token请求地址，如需开启OAuth2 password 类型登陆则必传此参数
		 */
		private String tokenUrl = "";

	}

	@Data
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
