package com.hccake.ballcat.common.swagger.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import springfox.documentation.spi.DocumentationType;

/**
 * 文档 swagger 版本
 * @author Hccake 2021/1/21
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum DocumentationTypeEnum {

	/**
	 * swagger2.0
	 */
	SWAGGER_2(DocumentationType.SWAGGER_2),

	/**
	 * swagger 3.0 openApi
	 */
	OAS_30(DocumentationType.OAS_30);

	private final DocumentationType type;

}
