package com.hccake.extend.dingtalk.message;

import com.hccake.ballcat.common.core.markdown.MarkdownBuilder;
import com.hccake.extend.dingtalk.DingTalkParams;
import com.hccake.extend.dingtalk.enums.ActionBtnOrientationEnum;
import com.hccake.extend.dingtalk.enums.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

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
	 *
	 * @author lingting 2020-06-10 23:59:45
	 */
	public DingTalkActionCardMessage addButton(String title, String url) {
		buttons.add(new Button(title, url));
		return this;
	}

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.ACTION_CARD;
	}

	@Override
	public DingTalkParams put(DingTalkParams params) {
		DingTalkParams.ActionCard card = new DingTalkParams.ActionCard().setTitle(title).setText(text.build())
				.setBtnOrientation(orientation.getVal());

		// 当 单按钮的 文本和链接都不为空时
		if (buttons.size() == 0) {
			card.setSingleTitle(singleTitle).setSingleUrl(singleUrl);
		}
		else {
			card.setButtons(buttons);
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
