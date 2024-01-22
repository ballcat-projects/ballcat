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
import java.io.FileOutputStream;
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
public final class FileUtils {

	private FileUtils() {
	}

	/**
	 * 系统的临时文件夹
	 */
	private static File tempDir = SystemUtils.tmpDirBallcat();

	/**
	 * 更新临时文件路径
	 */
	public static void updateTmpDir(String dirName) {
		tempDir = new File(SystemUtils.tmpDir(), dirName);
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
	 * 创建指定文件夹, 已存在时不会重新创建
	 * @param dir 文件夹.
	 * @throws IOException 创建失败时抛出
	 */
	public static void createDir(File dir) throws IOException {
		if (dir.exists()) {
			return;
		}

		if (!dir.mkdirs()) {
			throw new IOException("文件夹创建失败! 文件夹路径: " + dir.getAbsolutePath());
		}
	}

	/**
	 * 创建指定文件, 已存在时不会重新创建
	 * @param file 文件.
	 * @throws IOException 创建失败时抛出
	 */
	public static void createFile(File file) throws IOException {
		if (file.exists()) {
			return;
		}

		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			throw new IOException("父文件创建失败! 文件路径: " + file.getAbsolutePath());
		}

		if (!file.createNewFile()) {
			throw new IOException("文件创建失败! 文件路径: " + file.getAbsolutePath());
		}
	}

	/**
	 * 创建临时文件
	 */
	public static File createTemp() throws IOException {
		return createTemp("ballcat");
	}

	/**
	 * 创建临时文件
	 * @param trait 文件特征
	 * @return 临时文件对象
	 */
	public static File createTemp(String trait) throws IOException {
		return createTemp(trait, tempDir);
	}

	/**
	 * 创建临时文件
	 * @param trait 文件特征
	 * @param dir 文件存放位置
	 * @return 临时文件对象
	 */
	public static File createTemp(String trait, File dir) throws IOException {
		try {
			createDir(dir);
		}
		catch (IOException e) {
			throw new IOException("临时文件夹创建失败! 文件夹地址: " + dir.getAbsolutePath(), e);
		}

		return File.createTempFile(trait, "tmp", dir);
	}

	public static File createTemp(InputStream in) throws IOException {
		File file = createTemp();

		try (FileOutputStream out = new FileOutputStream(file)) {
			StreamUtils.write(in, out);
		}

		return file;
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

	public static boolean delete(File file) {
		try {
			Files.delete(file.toPath());
			return true;
		}
		catch (IOException e) {
			return false;
		}
	}

	/**
	 * 文件扩展名
	 * @param filename 文件名
	 * @return 扩展名
	 */
	public static String getExtension(String filename) {
		if (filename == null) {
			return null;
		}
		else {
			String name = (new File(filename)).getName();
			int extensionPosition = name.lastIndexOf(".");
			return extensionPosition < 0 ? "" : name.substring(extensionPosition + 1);
		}
	}

	/**
	 * 获取文件名，不带后缀
	 * @param filename 文件名
	 * @return 文件名
	 */
	public static String getBaseName(String filename) {
		if (filename == null) {
			return null;
		}
		else {
			String name = (new File(filename)).getName();
			int extensionPosition = name.lastIndexOf(".");
			return extensionPosition < 0 ? name : name.substring(0, extensionPosition);
		}
	}

}
