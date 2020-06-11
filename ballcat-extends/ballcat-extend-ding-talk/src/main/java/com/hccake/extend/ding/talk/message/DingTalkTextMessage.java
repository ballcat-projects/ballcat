package com.hccake.extend.ding.talk.message;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
public class DingTalkTextMessage extends AbstractDingTalkMessage {
	/**
	 * 消息内容
	 */
	private String content;

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.TEXT;
	}

	@Override
	public JSONObject json() {
		return JSONUtil.parseObj("{\"text\":{\"content\":\"" + content + "\"}}");
	}
}
