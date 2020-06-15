package com.hccake.extend.dingtalk.message;

import com.hccake.extend.dingtalk.DingTalkParams;
import com.hccake.extend.dingtalk.enums.MessageTypeEnum;

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
	 * 设置非公有属性
	 *
	 * @param params 已设置完公有参数的参数类
	 * @return 已设置完成的参数类
	 * @author lingting  2020-06-10 22:11:04
	 */
	public abstract DingTalkParams put(DingTalkParams params);


	@Override
	public String generate() {
		DingTalkParams params = put(new DingTalkParams()
				.setType(getType().getVal())
				.setAt(new DingTalkParams.At().setAtAll(atAll).setAtMobiles(atPhones))
		);
		return params.toString();
	}
}
