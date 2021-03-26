package com.hccake.ballcat.admin.websocket.message;

import com.hccake.ballcat.admin.websocket.constant.MessageTypeConstants;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.common.websocket.message.AbstractJsonWebSocketMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lingting 2021/3/25 17:23
 */
@Getter
@Setter
public class LovChangeMessage extends AbstractJsonWebSocketMessage {

	private String keyword;

	public LovChangeMessage(String keyword) {
		super(MessageTypeConstants.LOV_CHANGE);
		this.keyword = keyword;
	}

	public static LovChangeMessage of(String keyword) {
		return new LovChangeMessage(keyword);
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}

}
