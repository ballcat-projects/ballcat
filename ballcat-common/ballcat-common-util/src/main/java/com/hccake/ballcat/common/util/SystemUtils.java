package com.hccake.ballcat.common.util;

import java.io.File;
import java.nio.charset.Charset;
import lombok.experimental.UtilityClass;
import sun.awt.OSInfo;

/**
 * @author lingting 2022/6/25 12:10
 */
@UtilityClass
public class SystemUtils {

	/**
	 * 当前系统是否为Windows系统, 参考以下系统API
	 * @see OSInfo#getOSType()
	 * @return boolean
	 */
	public static boolean isWindows() {
		return System.getProperty("os.name").contains("Windows");
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

	public static File tempDir() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

}
