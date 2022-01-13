package com.hccake.ballcat.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author lingting 2021/4/16 14:33
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

	private static final File SYSTEM_TEMP_DIR = new File(System.getProperty("java.io.tmpdir"));

	/**
	 * 系统的临时文件夹
	 */
	private static File tempDir;

	static {
		updateTmpDir("ballcat");
	}

	public static File getSystemTempDir() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	/**
	 * 更新临时文件路径
	 * @author lingting 2021-10-18 17:10
	 */
	public static void updateTmpDir(String dirName) {
		tempDir = new File(SYSTEM_TEMP_DIR, dirName);

		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}
	}

	/**
	 * 获取临时文件, 不会创建文件
	 * @author lingting 2021-04-19 10:48
	 */
	public static File getTemplateFile(String name) throws IOException {
		File file;

		do {
			file = new File(tempDir, System.currentTimeMillis() + "." + name);
		}
		while (!file.createNewFile());

		return file;
	}

	/**
	 * 根据网络路径获取文件输入流
	 */
	public static InputStream getInputStreamByUrlPath(String path) throws IOException {
		// 从文件链接里获取文件流
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为3秒
		conn.setConnectTimeout(5 * 1000);
		// 得到输入流
		return conn.getInputStream();
	}

}
