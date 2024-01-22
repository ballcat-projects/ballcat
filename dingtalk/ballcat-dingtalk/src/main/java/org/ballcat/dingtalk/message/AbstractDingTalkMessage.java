/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.dingtalk.message;

import java.util.HashSet;
import java.util.Set;

import org.ballcat.dingtalk.DingTalkParams;
import org.ballcat.dingtalk.enums.MessageTypeEnum;

/**
 * 钉钉消息基础类
 *
 * @author lingting 2020/6/10 21:28
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
		this.atAll = true;
		return this;
	}

	/**
	 * 添加 at 对象的手机号
	 */
	public AbstractDingTalkMessage addPhone(String phone) {
		this.atPhones.add(phone);
		return this;
	}

	/**
	 * 获取消息类型
	 * @return 返回消息类型
	 */
	public abstract MessageTypeEnum getType();

	/**
	 * 设置非公有属性
	 * @param params 已设置完公有参数的参数类
	 * @return 已设置完成的参数类
	 */
	public abstract DingTalkParams put(DingTalkParams params);

	@Override
	public String generate() {
		DingTalkParams params = put(new DingTalkParams().setType(getType().getVal())
			.setAt(new DingTalkParams.At().setAtAll(this.atAll).setAtMobiles(this.atPhones)));
		return params.toString();
	}

}
