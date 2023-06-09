package org.ballcat.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作系统
 *
 * @author <a href="mailto:cs.liaow@gmail.com">evil0th</a> Create on 2023/6/8
 */
@Getter
@AllArgsConstructor
public enum OS {

	WINDOWS_10_2016("Windows 10+ / Windows Server 2016+", "windows nt 10\\.0", "windows nt (10\\.0)"),
	WINDOWS_81_2012R2("Windows 8.1 / Windows Server 2012R2", "windows nt 6\\.3", "windows nt (6\\.3)"),
	WINDOWS_8_2012("Windows 8 / Windows Server 2012", "windows nt 6\\.2", "windows nt (6\\.2)"),
	WINDOWS_VISTA("Windows Vista", "windows nt 6\\.0", "windows nt (6\\.0)"),
	WINDOWS_7_2008R2("Windows 7 / Windows Server 2008R2", "windows nt 6\\.1", "windows nt (6\\.1)"),
	WINDOWS_2003("Windows 2003", "windows nt 5\\.2", "windows nt (5\\.2)"),
	WINDOWS_XP("Windows XP", "windows nt 5\\.1", "windows nt (5\\.1)"),
	WINDOWS_2000("Windows 2000", "windows nt 5\\.0", "windows nt (5\\.0)"),
	WINDOWS_PHONE("Windows Phone", "windows (ce|phone|mobile)( os)?", "windows (?:ce|phone|mobile) (\\d+([._]\\d+)*)"),
	WINDOWS("Windows", "windows", null), DARWIN("Darwin", "Darwin\\/([\\d\\w\\.\\-]+)", "Darwin\\/([\\d\\w\\.\\-]+)"),
	OSX("OSX", "os x (\\d+)[._](\\d+)", "os x (\\d+([._]\\d+)*)"),
	ANDROID("Android", "Android", "Android (\\d+([._]\\d+)*)"),
	IPAD("iPad", "\\(iPad.*os (\\d+)[._](\\d+)", "\\(iPad.*os (\\d+([._]\\d+)*)"),
	IPHONE("iPhone", "\\(iPhone.*os (\\d+)[._](\\d+)", "\\(iPhone.*os (\\d+([._]\\d+)*)"),
	LINUX("Linux", "linux", null);

	private final String name;

	private final String regex;

	private final String versionRegex;

}
