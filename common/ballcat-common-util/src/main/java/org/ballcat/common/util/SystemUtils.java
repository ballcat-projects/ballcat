/*
 * Copyright 2023 the original author or authors.
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

import lombok.experimental.UtilityClass;

import java.io.File;
import java.nio.charset.Charset;

/**
 * @author lingting 2022/6/25 12:10
 */
@UtilityClass
public class SystemUtils {

	/**
	 * 当前系统是否为Windows系统, 参考以下系统API
	 * @see sun.awt.OSInfo#getOSType()
	 * @return boolean
	 */
	public boolean isWindows() {
		return osName().contains("Windows");
	}

	public boolean isLinux() {
		return osName().contains("Linux");
	}

	public boolean isMacX() {
		return osName().contains("OS X");
	}

	public boolean isMac() {
		return osName().contains("Mac OS");
	}

	public boolean isAix() {
		return osName().contains("AIX");
	}

	public String osName() {
		return System.getProperty("os.name");
	}

	/**
	 * 获取系统字符集
	 */
	public Charset charset() {
		return Charset.forName(System.getProperty("sun.jnu.encoding"));
	}

	public String lineSeparator() {
		return System.lineSeparator();
	}

	public String fileSeparator() {
		return File.separator;
	}

	/**
	 * @see SystemUtils#tmpDir()
	 * @deprecated 更换了方法名
	 */
	@Deprecated
	public File tempDir() {
		return tmpDir();
	}

	public File tmpDir() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	public File tmpDirBallcat() {
		return new File(System.getProperty("java.io.tmpdir"), "ballcat");
	}

	public File homeDir() {
		return new File(System.getProperty("user.home"));
	}

	public File homeDirBallcat() {
		return new File(System.getProperty("user.home"), ".ballcat");
	}

	public File workDir() {
		return new File(System.getProperty("user.dir"));
	}

	public String username() {
		return System.getProperty("user.name");
	}

}
