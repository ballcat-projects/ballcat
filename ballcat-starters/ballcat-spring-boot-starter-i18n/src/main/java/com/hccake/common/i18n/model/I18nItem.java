package com.hccake.common.i18n.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * i18n item
 *
 * @author Yakir
 */
@Accessors(chain = true)
@Data
public class I18nItem {

	/**
	 * 系统名称
	 */
	private String systemName;

	/**
	 * 业务码
	 */
	private String businessCode;

	/**
	 * 分组code
	 */
	private String code;

	/**
	 * 语言环境
	 */
	private String language;

	/**
	 * 值
	 */
	private String value;

	/**
	 * 类型
	 */
	private Integer type;

}
