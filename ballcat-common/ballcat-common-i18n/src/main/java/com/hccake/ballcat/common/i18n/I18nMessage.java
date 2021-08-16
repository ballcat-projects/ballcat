package com.hccake.ballcat.common.i18n;

import lombok.Data;

/**
 * 对标于 message bundle 的文件消息的抽象
 *
 * @author hccake
 */
@Data
public class I18nMessage {

	/**
	 * 唯一标识
	 */
	private String code;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 地区语言标签
	 */
	private String languageTag;

}
