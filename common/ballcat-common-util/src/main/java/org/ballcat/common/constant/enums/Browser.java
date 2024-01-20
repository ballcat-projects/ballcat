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

package org.ballcat.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 浏览器
 *
 * @author <a href="mailto:cs.liaow@gmail.com">evil0th</a> Create on 2023/6/8
 */
@Getter
@AllArgsConstructor
public enum Browser {

	// 主流浏览器
	CHROME("Chrome", "chrome", "Chrome[\\/ ]([\\d\\w\\.\\-]+)"),
	FIREFOX("Firefox", "firefox", "Firefox[\\/ ]([\\d\\w\\.\\-]+)"),
	SAFARI("Safari", "safari", "version\\/([\\d\\w\\.\\-]+)"), OPERA("Opera", "opera", "Opera[\\/ ]([\\d\\w\\.\\-]+)"),
	EDGE("Edge", "Edge|Edg", "(?:edge|Edg|EdgA)\\/([\\d\\w\\.\\-]+)"), IE("IE", "msie", "msie ([\\d\\w\\.\\-]+)"),
	IE11("IE11", "rv:11", "rv:([\\d\\w\\.\\-]+)"), UC("UCBrowser", "UC?Browser", "UC?Browser\\/([\\d\\w\\.\\-]+)"),
	QQ("QQBrowser", "MQQBrowser", "MQQBrowser\\/([\\d\\w\\.\\-]+)"),
	QUARK("Quark", "Quark", "Quark[\\/ ]([\\d\\w\\.\\-]+)"),
	ANDROID("Android", "android", "version\\/([\\d\\w\\.\\-]+)"),
	// 厂商浏览器
	WECHAT("Wechat", "MicroMessenger", "MicroMessenger[\\/ ]([\\d\\w\\.\\-]+)"),
	WX_WORK("WxWork", "wxwork", "wxwork\\/([\\d\\w\\.\\-]+)"),
	MINI_PROGRAM("MiniProgram", "miniProgram", "miniProgram[\\/ ]([\\d\\w\\.\\-]+)"),
	DING_TALK("DingTalk", "DingTalk", "AliApp\\(DingTalk\\/([\\d\\w\\.\\-]+)\\)"),
	DING_TALK_WIN("DingTalk-win", "dingtalk-win", "DingTalk\\(([\\d\\w\\.\\-]+)\\)"),
	ALIPAY("Alipay", "AlipayClient", "AliApp\\(AP\\/([\\d\\w\\.\\-]+)\\)"),
	TAOBAO("Taobao", "taobao", "AliApp\\(TB\\/([\\d\\w\\.\\-]+)\\)"),
	MIUI("Miui", "MiuiBrowser|mibrowser", "MiuiBrowser\\/([\\d\\w\\.\\-]+)"),
	LENOVO("Lenovo", "SLBrowser", "SLBrowser/([\\d\\w\\.\\-]+)"),
	// 以下 其他
	KONQUEROR("Konqueror", "konqueror", "Konqueror[\\/ ]([\\d\\w\\.\\-]+)"),
	LOTUS("Lotus", "lotus.notes", "Lotus-Notes\\/([\\w.]+)"),
	THUNDERBIRD("Thunderbird", "thunderbird", "Thunderbird[\\/ ]([\\d\\w\\.\\-]+)"),
	SEAMONKEY("Seamonkey", "seamonkey", "Seamonkey[\\/ ]([\\d\\w\\.\\-]+)"),
	OUTLOOK("Outlook", "microsoft.outlook", "Outlook[\\/ ]([\\d\\w\\.\\-]+)"),
	EVOLUTION("Evolution", "evolution", "Evolution[\\/ ]([\\d\\w\\.\\-]+)"),
	GABBLE("Gabble", "Gabble", "Gabble[\\/ ]([\\d\\w\\.\\-]+)"),
	YAMMER_DESKTOP("Yammer Desktop", "AdobeAir", "([\\d\\w\\.\\-]+)\\/Yammer"),
	YAMMER_MOBILE("Yammer Mobile", "Yammer[\\s]+([\\d\\w\\.\\-]+)", "Yammer[\\s]+([\\d\\w\\.\\-]+)");

	private final String name;

	private final String regex;

	private final String versionRegex;

}
