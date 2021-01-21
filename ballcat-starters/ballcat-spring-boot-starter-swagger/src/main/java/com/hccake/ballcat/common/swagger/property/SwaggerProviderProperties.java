package com.hccake.ballcat.common.swagger.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/11/1 20:05
 */
@Data
@ConfigurationProperties("ballcat.swagger.provider")
public class SwaggerProviderProperties {

	/**
	 * 聚合者的来源，用于控制跨域放行
	 */
	private String aggregatorOrigin = "*";

}
