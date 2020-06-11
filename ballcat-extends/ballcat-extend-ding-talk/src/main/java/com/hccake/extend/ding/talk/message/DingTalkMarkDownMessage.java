package com.hccake.extend.ding.talk.message;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hccake.ballcat.common.core.markdown.MarkdownBuilder;
import com.hccake.extend.ding.talk.enums.MessageTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author lingting  2020/6/10 22:13
 */
@Getter
@Setter
@Accessors(chain = true)
public class DingTalkMarkDownMessage extends AbstractDingTalkMessage {
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private MarkdownBuilder text;

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.MARKDOWN;
	}

	@Override
	public JSONObject json() {
		return JSONUtil.parseObj("{\"markdown\":{\"title\":\"" + title + "\",\"text\":\"" + text.build() + "\"}}");
	}
}
