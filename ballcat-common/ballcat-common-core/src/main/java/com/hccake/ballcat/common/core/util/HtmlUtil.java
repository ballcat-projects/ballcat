package com.hccake.ballcat.common.core.util;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * @author Hccake 2020/12/21
 * @version 1.0
 */
public class HtmlUtil {

	/**
	 * html 转字符串，保留换行样式
	 * @link https://stackoverflow.com/questions/5640334/how-do-i-preserve-line-breaks-when-using-jsoup-to-convert-html-to-plain-text
	 * @param html html字符串
	 * @param mergeLineBreak 是否合并换行符
	 * @return 保留换行格式的纯文本
	 */
	public static String toText(String html, boolean mergeLineBreak) {
		if (StrUtil.isEmpty(html)) {
			return html;
		}
		Document document = Jsoup.parse(html);
		// makes html() preserve linebreaks and spacing
		document.outputSettings(new Document.OutputSettings().prettyPrint(false));
		document.select("br").append("\\n");
		document.select("p").prepend("\\n\\n");
		String s = document.html().replaceAll("\\\\n", "\n");
		String result = Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
		// 合并多个换行
		return mergeLineBreak ? result.replaceAll("(\r?\n(\\s*\r?\n)+)", "\n") : result;
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
