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

import java.io.File;
import java.nio.charset.Charset;

/**
 * @author lingting 2022/6/25 12:10
 */
public final class SystemUtils {

	private SystemUtils() {
	}

	/**
	 * 当前系统是否为Windows系统, 参考以下系统API
	 * @see sun.awt.OSInfo#getOSType()
	 * @return boolean
	 */
	public static boolean isWindows() {
		return osName().contains("Windows");
	}

	public static boolean isLinux() {
		return osName().contains("Linux");
	}

	public static boolean isMacX() {
		return osName().contains("OS X");
	}

	public static boolean isMac() {
		return osName().contains("Mac OS");
	}

	public static boolean isAix() {
		return osName().contains("AIX");
	}

	public static String osName() {
		return System.getProperty("os.name");
	}

	/**
	 * 获取系统字符集
	 */
	public static Charset charset() {
		return Charset.forName(System.getProperty("sun.jnu.encoding"));
	}

	public static String lineSeparator() {
		return System.lineSeparator();
	}

	public static String fileSeparator() {
		return File.separator;
	}

	/**
	 * @see SystemUtils#tmpDir()
	 * @deprecated 更换了方法名
	 */
	@Deprecated
	public static File tempDir() {
		return tmpDir();
	}

	public static File tmpDir() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	public static File tmpDirBallcat() {
		return new File(System.getProperty("java.io.tmpdir"), "ballcat");
	}

	public static File homeDir() {
		return new File(System.getProperty("user.home"));
	}

	public static File homeDirBallcat() {
		return new File(System.getProperty("user.home"), ".ballcat");
	}

	public static File workDir() {
		return new File(System.getProperty("user.dir"));
	}

	public static String username() {
		return System.getProperty("user.name");
	}

}
