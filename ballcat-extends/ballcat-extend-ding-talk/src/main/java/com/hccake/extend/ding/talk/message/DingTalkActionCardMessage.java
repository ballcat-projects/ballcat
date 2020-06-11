package com.hccake.extend.ding.talk.message;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hccake.ballcat.common.core.markdown.MarkdownBuilder;
import com.hccake.extend.ding.talk.enums.ActionBtnOrientationEnum;
import com.hccake.extend.ding.talk.enums.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 跳转 ActionCard类型
 *
 * @author lingting  2020/6/10 23:39
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
	 * @author lingting  2020-06-10 23:59:45
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
	public JSONObject json() {
		// 头
		String jsonStr = "{\"actionCard\":{" +
				"\"title\":\"" + title + "\"," +
				"\"text\":\"" + text.build() + "\"," +
				"\"btnOrientation\":\"" + orientation.getVal() + "\",";

		// 当 单按钮的  文本和链接都不为空时
		if (buttons.size() == 0) {
			jsonStr += "\"singleTitle\":\"" + singleTitle + "\"," +
					"\"singleURL\":\"" + singleUrl + "\",";
		} else {
			// 否则使用自定义按钮组
			jsonStr += "\"btns\":" + JSONUtil.toJsonStr(buttons);
		}

		// 尾
		jsonStr += "}}";
		return JSONUtil.parseObj(jsonStr);
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
