package com.hccake.ballcat.admin.i18n.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * i18n admin properties
 *
 * @author Yakir
 */
@Component
@Data
@ConfigurationProperties(prefix = "ballcat.i18n")
public class I18nAdminProperties {

	/**
	 * 请求头指定语言key
	 */
	private String langHeader = "lang";

	/**
	 * msg响应key code
	 */
	private String responseMsgKey = "response_msg";

}
