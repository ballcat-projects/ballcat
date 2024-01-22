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

package org.ballcat.common.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;

/**
 * @author Hccake 2020/12/21
 *
 */
public final class HtmlUtils {

	private HtmlUtils() {
	}

	/**
	 * html 转字符串，保留换行样式
	 * @link <a href=
	 * "https://stackoverflow.com/questions/5640334/how-do-i-preserve-line-breaks-when-using-jsoup-to-convert-html-to-plain-text">how-do-i-preserve-line-breaks</a>
	 * @param html html字符串
	 * @param mergeLineBreak 是否合并换行符
	 * @return 保留换行格式的纯文本
	 */
	public static String toText(String html, boolean mergeLineBreak) {
		if (!StringUtils.hasText(html)) {
			return html;
		}
		Document document = Jsoup.parse(html);
		// makes html() preserve linebreaks and spacing
		document.outputSettings(new Document.OutputSettings().prettyPrint(true));
		String result = document.wholeText();

		// 合并多个换行
		if (mergeLineBreak) {
			int oldLength;
			do {
				oldLength = result.length();
				result = result.replace('\r', '\n');
				result = result.replace("\n\n", "\n");
			}
			while (result.length() != oldLength);
		}

		return result;
	}

	/**
	 * html 转字符串，保留换行样式，默认合并换行符
	 * @param html html字符串
	 * @return 保留换行格式的纯文本
	 */
	public static String toText(String html) {
		return toText(html, true);
	}

}
