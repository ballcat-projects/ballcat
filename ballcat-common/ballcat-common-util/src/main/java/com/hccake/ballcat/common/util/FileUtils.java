package com.hccake.ballcat.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lingting 2021/4/16 14:33
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

	private static final File SYSTEM_TEMP_DIR = SystemUtils.tempDir();

	/**
	 * 系统的临时文件夹
	 */
	private static File tempDir;

	static {
		updateTmpDir("ballcat");
	}

	/**
	 * use {@link SystemUtils#tempDir()}
	 */
	@Deprecated
	public static File getSystemTempDir() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	/**
	 * 更新临时文件路径
	 */
	public static void updateTmpDir(String dirName) {
		tempDir = new File(SYSTEM_TEMP_DIR, dirName);
	}

	/**
	 * 获取临时文件, 不会创建文件
	 */
	public static File getTemplateFile(String name) throws IOException {
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}

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

	/**
	 * 扫描指定路径下所有文件
	 * @param path 指定路径
	 * @param recursive 是否递归
	 * @return java.util.List<java.lang.String>
	 */
	public static List<String> scanFile(String path, boolean recursive) {
		List<String> list = new ArrayList<>();
		File file = new File(path);
		if (!file.exists()) {
			return list;
		}

		if (file.isFile()) {
			list.add(file.getAbsolutePath());
		}
		// 文件夹
		else {
			File[] files = file.listFiles();

			if (files == null || files.length < 1) {
				return list;
			}

			for (File childFile : files) {
				// 如果递归
				if (recursive && childFile.isDirectory()) {
					list.addAll(scanFile(childFile.getAbsolutePath(), true));
				}
				// 是文件
				else if (childFile.isFile()) {
					list.add(childFile.getAbsolutePath());
				}
			}
		}

		return list;
	}

	/**
	 * 复制文件
	 * @param source 源文件
	 * @param target 目标文件
	 * @param override 如果目标文件已存在是否覆盖
	 * @param options 其他文件复制选项 {@link StandardCopyOption}
	 * @return 目标文件地址
	 */
	public static Path copy(File source, File target, boolean override, CopyOption... options) throws IOException {
		List<CopyOption> list = new ArrayList<>();
		if (override) {
			list.add(StandardCopyOption.REPLACE_EXISTING);
		}

		if (options != null && options.length > 0) {
			list.addAll(Arrays.asList(options));
		}

		return Files.copy(source.toPath(), target.toPath(), list.toArray(new CopyOption[0]));
	}

}
