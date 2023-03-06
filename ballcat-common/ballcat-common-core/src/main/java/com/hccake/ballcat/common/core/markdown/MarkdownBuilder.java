package com.hccake.ballcat.common.core.markdown;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 生成 markdown 文本
 *
 * @author lingting 2020/6/10 22:43
 */
public class MarkdownBuilder {

	public static final String TITLE_PREFIX = "#";

	public static final String QUOTE_PREFIX = "> ";

	public static final String BOLD_PREFIX = "**";

	public static final String ITALIC_PREFIX = "*";

	public static final String UNORDERED_LIST_PREFIX = "- ";

	public static final String ORDER_LIST_PREFIX = ". ";

	/**
	 * 存放内容
	 */
	private final List<String> content = new ArrayList<>();

	/**
	 * 当前操作行文本
	 */
	private StringBuilder lineTextBuilder;

	public MarkdownBuilder() {
		this.lineTextBuilder = new StringBuilder();
	}

	/**
	 * 添加自定义内容
	 * @param content 自定义内容
	 */
	public MarkdownBuilder append(String content) {
		lineTextBuilder.append(content);
		return this;
	}

	/**
	 * 有序列表 自动生成 索引
	 * @param content 文本
	 */
	public MarkdownBuilder orderList(String content) {
		// 获取最后一个字符串
		String tmp = "";
		if (!this.content.isEmpty()) {
			tmp = this.content.get(this.content.size() - 1);
		}
		// 索引
		int index = 1;

		// 校验 是否 为有序列表行的正则
		String isOrderListPattern = "^\\d\\. .*";
		if (Pattern.matches(isOrderListPattern, tmp)) {
			// 如果是数字开头
			index = Convert.toInt(tmp.substring(0, tmp.indexOf(ORDER_LIST_PREFIX) - 1));
		}
		return orderList(index, content);
	}

	/**
	 * 有序列表
	 * @param index 索引
	 * @param content 文本
	 */
	public MarkdownBuilder orderList(int index, String content) {
		lineBreak();
		lineTextBuilder.append(index).append(ORDER_LIST_PREFIX).append(content);
		return this;
	}

	/**
	 * 无序列表 - item1 - item2
	 */
	public MarkdownBuilder unorderedList(String content) {
		// 换行
		lineBreak();
		lineTextBuilder.append(UNORDERED_LIST_PREFIX).append(content);
		return this;
	}

	/**
	 * 图片
	 * @param url 图片链接
	 */
	public MarkdownBuilder pic(String url) {
		return pic(CharSequenceUtil.EMPTY, url);
	}

	/**
	 * 图片
	 * @param title 图片标题
	 * @param url 图片路径
	 */
	public MarkdownBuilder pic(String title, String url) {
		lineTextBuilder.append("![").append(title).append("](").append(url).append(")");
		return this;
	}

	/**
	 * 链接
	 * @param title 标题
	 * @param url http 路径
	 */
	public MarkdownBuilder link(String title, String url) {
		lineTextBuilder.append("[").append(title).append("](").append(url).append(")");
		return this;
	}

	/**
	 * 斜体
	 */
	public MarkdownBuilder italic(String content) {
		lineTextBuilder.append(ITALIC_PREFIX).append(content).append(ITALIC_PREFIX);
		return this;
	}

	/**
	 * 加粗
	 */
	public MarkdownBuilder bold(String content) {
		lineTextBuilder.append(BOLD_PREFIX).append(content).append(BOLD_PREFIX);
		return this;
	}

	/**
	 * 引用 > 文本
	 * @param content 文本
	 */
	public MarkdownBuilder quote(String content) {
		lineBreak();
		lineTextBuilder.append(QUOTE_PREFIX).append(content);
		return this;
	}

	/**
	 * 添加引用后 强制换行
	 */
	public MarkdownBuilder quoteLineBreak(String content) {
		quote(content);
		return forceLineBreak();
	}

	/**
	 * 添加引用后, 换行, 用于写入下一行引用
	 */
	public MarkdownBuilder quoteBreak(String content) {
		// 当前行引用内容
		quote(content);
		// 空引用行
		return quote("");
	}

	/**
	 * 强制换行
	 */
	public MarkdownBuilder forceLineBreak() {
		content.add(lineTextBuilder.toString());
		lineTextBuilder = new StringBuilder();
		return this;
	}

	/**
	 * 换行 当已编辑文本长度不为0时换行
	 */
	public MarkdownBuilder lineBreak() {
		if (lineTextBuilder.length() != 0) {
			return forceLineBreak();
		}
		return this;
	}

	/**
	 * 生成 i 级标题
	 *
	 * @author lingting 2020-06-10 22:55:39
	 */
	private MarkdownBuilder title(int i, String content) {
		// 如果当前操作行已有字符，需要换行
		lineBreak();
		for (int j = 0; j < i; j++) {
			lineTextBuilder.append(TITLE_PREFIX);
		}
		this.content.add(lineTextBuilder.append(" ").append(content).toString());
		lineTextBuilder = new StringBuilder();
		return this;
	}

	public MarkdownBuilder title1(String text) {
		return title(1, text);
	}

	public MarkdownBuilder title2(String text) {
		return title(2, text);
	}

	public MarkdownBuilder title3(String text) {
		return title(3, text);
	}

	public MarkdownBuilder title4(String text) {
		return title(4, text);
	}

	public MarkdownBuilder title5(String text) {
		return title(5, text);
	}

	@Override
	public String toString() {
		return build();
	}

	/**
	 * 构筑 Markdown 文本
	 */
	public String build() {
		lineBreak();
		StringBuilder res = new StringBuilder();
		content.forEach(line -> res.append(line).append(" \n"));
		return res.toString();
	}

}
