package com.hccake.extend.dingtalk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 钉钉消息类型
 *
 * @author lingting  2020/6/10 21:29
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {
	/**
	 * 消息值     消息说明
	 */
	TEXT("text", "文本"),
	LINK("link", "链接"),
	MARKDOWN("markdown", "markdown"),
	ACTION_CARD("actionCard", "跳转 actionCard 类型"),
	;
	private final String val;
	private final String desc;
}
