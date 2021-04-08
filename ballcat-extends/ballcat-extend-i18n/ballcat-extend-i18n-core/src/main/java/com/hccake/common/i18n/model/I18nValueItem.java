package com.hccake.common.i18n.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * i18n
 *
 * @author Yakir
 */
@Data
@Accessors(chain = true)
public class I18nValueItem {

	/**
	 * 模板值
	 */
	private String tplValue;

	/**
	 * 类型 1.明文 2.模板
	 */
	private Integer type;

}
