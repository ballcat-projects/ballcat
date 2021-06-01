package com.hccake.ballcat.oauth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/18 10:55 安全相关配置
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ballcat.security")
public class SecurityProperties {

	/**
	 * 前后端交互使用的对称加密算法的密钥，必须 16 位字符
	 */
	private String passwordSecretKey;

	/**
	 * 忽略鉴权的 url 列表
	 */
	private List<String> ignoreUrls = new ArrayList<>();

	/**
	 * 是否禁止嵌入iframe
	 */
	private boolean iframeDeny = true;

}
