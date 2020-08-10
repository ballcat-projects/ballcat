package com.hccake.ballcat.admin.oauth.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/18 10:55 资源服务器忽略鉴权的url地址
 */
@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.oauth2")
public class PermitAllUrlProperties {

	private List<String> ignoreUrls = new ArrayList<>();

	/**
	 * 是否禁止嵌入iframe
	 */
	private boolean iframeDeny = true;

}
