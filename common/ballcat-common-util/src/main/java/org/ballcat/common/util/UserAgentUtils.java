package org.ballcat.common.util;

import lombok.experimental.UtilityClass;
import org.ballcat.common.constant.enums.Browser;
import org.ballcat.common.constant.enums.OS;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * 根据浏览器user-agent猜测Browser和OS
 *
 * @author <a href="mailto:cs.liaow@gmail.com">evil0th</a> Create on 2023/6/8
 */
@UtilityClass
public class UserAgentUtils {

	public final String UNKNOWN_VALUE = "Unknown";

	public final String UNKNOWN_VERSION = "??";

	public final String UNKNOWN_NAME_VERSION = "Unknown ??";

	/**
	 * 根据 UA 猜测浏览器(版本)
	 * @param userAgent UA
	 * @return 结果
	 */
	public String detectBrowser(String userAgent) {
		return detectBrowser(userAgent, false);
	}

	/**
	 * 根据 UA 猜测浏览器(版本)
	 * @param userAgent UA
	 * @param withVersion 是否携带版本信息
	 * @return 结果
	 */
	public String detectBrowser(String userAgent, boolean withVersion) {
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
	public String detectOS(String userAgent) {
		return detectOS(userAgent, false);
	}

	/**
	 * 根据 UA 猜测浏览器(版本)
	 * @param userAgent UA
	 * @param withVersion 是否携带版本信息
	 * @return 结果
	 */
	public String detectOS(String userAgent, boolean withVersion) {
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

	public Browser parseBrowser(@NonNull String info) {
		for (Browser browser : Browser.values()) {
			if (RegexUtils.find(browser.getRegex(), info)) {
				return browser;
			}
		}
		return null;
	}

	public OS parseOS(@NonNull String info) {
		for (OS os : OS.values()) {
			if (RegexUtils.find(os.getRegex(), info)) {
				return os;
			}
		}
		return null;
	}

}
