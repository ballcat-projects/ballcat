package com.hccake.extend.ding.talk.message;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hccake.extend.ding.talk.enums.MessageTypeEnum;
import lombok.SneakyThrows;

import java.util.HashSet;
import java.util.Set;

/**
 * 钉钉消息基础类
 *
 * @author lingting  2020/6/10 21:28
 */
public abstract class AbstractDingTalkMessage implements DingTalkMessage {
	/**
	 * at 的人的手机号码
	 */
	private final Set<String> atPhones = new HashSet<>();
	/**
	 * 是否 at 所有人
	 */
	private boolean atAll = false;

	public AbstractDingTalkMessage atAll() {
		atAll = true;
		return this;
	}

	/**
	 * 添加 at 对象的手机号
	 *
	 * @author lingting  2020-06-10 21:57:08
	 */
	public AbstractDingTalkMessage addPhone(String phone) {
		atPhones.add(phone);
		return this;
	}

	/**
	 * 获取消息类型
	 *
	 * @return 返回消息类型
	 * @author lingting  2020-06-10 22:12:30
	 */
	public abstract MessageTypeEnum getType();

	/**
	 * 生成内容json
	 *
	 * @return 返回生成的json字符串
	 * @author lingting  2020-06-10 22:11:04
	 */
	public abstract JSONObject json();

	@Override
	@SneakyThrows
	public String toString() {
		JSONObject json = new JSONObject();
		json.put("msgtype", getType().getVal());
		json.putAll(json());
		json.put(
				"at",
				new JSONObject()
						.put("isAtAll", atAll)
						.put("atMobiles", JSONUtil.toJsonStr(atPhones))
		);
		return json.toString();
	}
}
