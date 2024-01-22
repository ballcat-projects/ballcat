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

package org.ballcat.common.markdown;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.ballcat.common.util.json.JacksonJsonToolAdapter;

/**
 * 生成 markdown 文本
 *
 * @author lingting 2020/6/10 22:43
 */
public class MarkdownBuilder {

	public static final String TITLE_PREFIX = "#";

	public static final String QUOTE_PREFIX = "> ";

	public static final String CODE_PREFIX = "``` ";

	public static final String CODE_SUFFIX = "```";

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
	public MarkdownBuilder append(Object content) {
		this.lineTextBuilder.append(toString(content));
		return this;
	}

	/**
	 * 有序列表 自动生成 索引
	 * @param content 文本
	 */
	public MarkdownBuilder orderList(Object content) {
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
			index = Integer.parseInt(tmp.substring(0, tmp.indexOf(ORDER_LIST_PREFIX) - 1));
		}
		return orderList(index, content);
	}

	/**
	 * 有序列表
	 * @param index 索引
	 * @param content 文本
	 */
	public MarkdownBuilder orderList(int index, Object content) {
		lineBreak();
		this.lineTextBuilder.append(index).append(ORDER_LIST_PREFIX).append(toString(content));
		return this;
	}

	/**
	 * 无序列表 - item1 - item2
	 */
	public MarkdownBuilder unorderedList(Object content) {
		// 换行
		lineBreak();
		this.lineTextBuilder.append(UNORDERED_LIST_PREFIX).append(toString(content));
		return this;
	}

	/**
	 * 图片
	 * @param url 图片链接
	 */
	public MarkdownBuilder pic(String url) {
		return pic("", url);
	}

	/**
	 * 图片
	 * @param title 图片标题
	 * @param url 图片路径
	 */
	public MarkdownBuilder pic(Object title, String url) {
		this.lineTextBuilder.append("![").append(title).append("](").append(url).append(")");
		return this;
	}

	/**
	 * 链接
	 * @param title 标题
	 * @param url http 路径
	 */
	public MarkdownBuilder link(Object title, String url) {
		this.lineTextBuilder.append("[").append(title).append("](").append(url).append(")");
		return this;
	}

	/**
	 * 斜体
	 */
	public MarkdownBuilder italic(Object content) {
		this.lineTextBuilder.append(ITALIC_PREFIX).append(toString(content)).append(ITALIC_PREFIX);
		return this;
	}

	/**
	 * 加粗
	 */
	public MarkdownBuilder bold(Object content) {
		this.lineTextBuilder.append(BOLD_PREFIX).append(toString(content)).append(BOLD_PREFIX);
		return this;
	}

	/**
	 * 引用 > 文本
	 * @param content 文本
	 */
	public MarkdownBuilder quote(Object... content) {
		lineBreak();
		this.lineTextBuilder.append(QUOTE_PREFIX);
		for (Object o : content) {
			this.lineTextBuilder.append(toString(o));
		}
		return this;
	}

	/**
	 * 添加引用后, 换行, 写入下一行引用
	 */
	public MarkdownBuilder quoteBreak(Object... content) {
		// 当前行引用内容
		quote(content);
		// 空引用行
		return quote();
	}

	/**
	 * 代码
	 */
	public MarkdownBuilder code(String type, Object... code) {
		lineBreak();
		this.lineTextBuilder.append(CODE_PREFIX).append(type);
		lineBreak();
		for (Object o : code) {
			this.lineTextBuilder.append(toString(o));
		}
		lineBreak();
		this.lineTextBuilder.append(CODE_SUFFIX);
		return lineBreak();
	}

	/**
	 * 代码
	 */
	public MarkdownBuilder json(Object obj) {
		String json;
		if (obj instanceof String) {
			json = (String) obj;
		}
		else {
			json = multiJson(obj);
		}
		return code("json", json);
	}

	@SneakyThrows
	private String multiJson(Object obj) {
		ObjectMapper mapper = JacksonJsonToolAdapter.getMapper().copy().enable(SerializationFeature.INDENT_OUTPUT);
		return mapper.writeValueAsString(obj);
	}

	/**
	 * 强制换行
	 */
	public MarkdownBuilder forceLineBreak() {
		this.content.add(this.lineTextBuilder.toString());
		this.lineTextBuilder = new StringBuilder();
		return this;
	}

	/**
	 * 换行 当已编辑文本长度不为0时换行
	 */
	public MarkdownBuilder lineBreak() {
		if (this.lineTextBuilder.length() != 0) {
			return forceLineBreak();
		}
		return this;
	}

	/**
	 * 生成 i 级标题
	 *
	 * @author lingting 2020-06-10 22:55:39
	 */
	private MarkdownBuilder title(int i, Object content) {
		// 如果当前操作行已有字符，需要换行
		lineBreak();
		for (int j = 0; j < i; j++) {
			this.lineTextBuilder.append(TITLE_PREFIX);
		}
		this.content.add(this.lineTextBuilder.append(" ").append(toString(content)).toString());
		this.lineTextBuilder = new StringBuilder();
		return this;
	}

	public MarkdownBuilder title1(Object text) {
		return title(1, text);
	}

	public MarkdownBuilder title2(Object text) {
		return title(2, text);
	}

	public MarkdownBuilder title3(Object text) {
		return title(3, text);
	}

	public MarkdownBuilder title4(Object text) {
		return title(4, text);
	}

	public MarkdownBuilder title5(Object text) {
		return title(5, text);
	}

	String toString(Object o) {
		if (o == null) {
			return "";

		}
		return o.toString();
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
		this.content.forEach(line -> res.append(line).append(" \n"));
		return res.toString();
	}

}
