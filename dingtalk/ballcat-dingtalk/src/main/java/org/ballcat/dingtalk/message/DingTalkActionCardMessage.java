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

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.ballcat.common.markdown.MarkdownBuilder;
import org.ballcat.dingtalk.DingTalkParams;
import org.ballcat.dingtalk.enums.ActionBtnOrientationEnum;
import org.ballcat.dingtalk.enums.MessageTypeEnum;

/**
 * 跳转 ActionCard类型
 *
 * @author lingting 2020/6/10 23:39
 */
@Getter
@Setter
@Accessors(chain = true)
public class DingTalkActionCardMessage extends AbstractDingTalkMessage {

	private String title;

	/**
	 * 内容
	 */
	private MarkdownBuilder text;

	/**
	 * 按钮排列样式 默认横
	 */
	private ActionBtnOrientationEnum orientation = ActionBtnOrientationEnum.HORIZONTAL;

	/**
	 * 单个按钮的标题
	 */
	private String singleTitle;

	/**
	 * 点击singleTitle按钮触发的URL
	 */
	private String singleUrl;

	/**
	 * 自定义按钮组 如果配置了 按钮组 则 单按钮配置无效
	 */
	private List<Button> buttons = new ArrayList<>();

	/**
	 * 添加按钮
	 */
	public DingTalkActionCardMessage addButton(String title, String url) {
		this.buttons.add(new Button(title, url));
		return this;
	}

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.ACTION_CARD;
	}

	@Override
	public DingTalkParams put(DingTalkParams params) {
		DingTalkParams.ActionCard card = new DingTalkParams.ActionCard().setTitle(this.title)
			.setText(this.text.build())
			.setBtnOrientation(this.orientation.getVal());

		// 当 单按钮的 文本和链接都不为空时
		if (this.buttons.isEmpty()) {
			card.setSingleTitle(this.singleTitle).setSingleUrl(this.singleUrl);
		}
		else {
			card.setButtons(this.buttons);
		}
		return params.setActionCard(card);
	}

	@Getter
	@AllArgsConstructor
	public static class Button {

		/**
		 * 标题
		 */
		private final String title;

		/**
		 * 跳转路径
		 */
		private final String actionURL;

	}

}
