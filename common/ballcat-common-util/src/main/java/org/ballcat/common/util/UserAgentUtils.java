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

import org.ballcat.common.constant.enums.Browser;
import org.ballcat.common.constant.enums.OS;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * 根据浏览器user-agent猜测Browser和OS
 *
 * @author <a href="mailto:cs.liaow@gmail.com">evil0th</a> Create on 2023/6/8
 */
public final class UserAgentUtils {

	private UserAgentUtils() {
	}

	public static final String UNKNOWN_VALUE = "Unknown";

	public static final String UNKNOWN_VERSION = "??";

	public static final String UNKNOWN_NAME_VERSION = "Unknown ??";

	/**
	 * 根据 UA 猜测浏览器(版本)
	 * @param userAgent UA
	 * @return 结果
	 */
	public static String detectBrowser(String userAgent) {
		return detectBrowser(userAgent, false);
	}

	/**
	 * 根据 UA 猜测浏览器(版本)
	 * @param userAgent UA
	 * @param withVersion 是否携带版本信息
	 * @return 结果
	 */
	public static String detectBrowser(String userAgent, boolean withVersion) {
		if (!StringUtils.hasText(userAgent)) {
			return withVersion ? UNKNOWN_NAME_VERSION : UNKNOWN_VALUE;
		}
		Browser browser = parseBrowser(userAgent);
		if (null == browser) {
			return withVersion ? UNKNOWN_NAME_VERSION : UNKNOWN_VALUE;
		}
		if (withVersion && null != browser.getVersionRegex()) {
			String version = RegexUtils.group(browser.getVersionRegex(), userAgent, 1);
			return String.format("%s(%s)", browser.getName(), version != null ? version : UNKNOWN_VERSION);
		}
		else {
			return browser.getName();
		}
	}

	/**
	 * 根据 UA 猜测浏览器(版本)
	 * @param userAgent UA
	 * @return 结果
	 */
	public static String detectOS(String userAgent) {
		return detectOS(userAgent, false);
	}

	/**
	 * 根据 UA 猜测浏览器(版本)
	 * @param userAgent UA
	 * @param withVersion 是否携带版本信息
	 * @return 结果
	 */
	public static String detectOS(String userAgent, boolean withVersion) {
		if (!StringUtils.hasText(userAgent)) {
			return withVersion ? UNKNOWN_NAME_VERSION : UNKNOWN_VALUE;
		}
		OS os = parseOS(userAgent);
		if (null == os) {
			return withVersion ? UNKNOWN_NAME_VERSION : UNKNOWN_VALUE;
		}
		if (withVersion && null != os.getVersionRegex()) {
			String version = RegexUtils.group(os.getVersionRegex(), userAgent, 1);
			return String.format("%s(%s)", os.getName(), version != null ? version : UNKNOWN_VERSION);
		}
		else {
			return os.getName();
		}
	}

	public static Browser parseBrowser(@NonNull String info) {
		for (Browser browser : Browser.values()) {
			if (RegexUtils.find(browser.getRegex(), info)) {
				return browser;
			}
		}
		return null;
	}

	public static OS parseOS(@NonNull String info) {
		for (OS os : OS.values()) {
			if (RegexUtils.find(os.getRegex(), info)) {
				return os;
			}
		}
		return null;
	}

}
