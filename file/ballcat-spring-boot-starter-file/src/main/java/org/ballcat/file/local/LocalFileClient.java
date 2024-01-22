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

package org.ballcat.file.local;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.ballcat.common.util.StreamUtils;
import org.ballcat.common.util.SystemUtils;
import org.ballcat.file.FileProperties.LocalProperties;
import org.ballcat.file.core.AbstractFileClient;
import org.ballcat.file.exception.FileException;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2021/10/17 20:11
 * @author 疯狂的狮子Li 2022-04-24
 */
public class LocalFileClient extends AbstractFileClient {

	private final File parentDir;

	public LocalFileClient(LocalProperties properties) throws IOException {
		final File dir = StringUtils.hasText(properties.getPath()) ? new File(properties.getPath())
				: SystemUtils.tmpDir();
		// 不存在且创建失败
		if (!dir.exists() && !dir.mkdirs()) {
			throw new FileException(String.format("路径: %s; 不存在且创建失败! 请检查是否拥有对该路径的操作权限!", dir.getPath()));
		}
		this.parentDir = dir;
		super.rootPath = dir.getPath();
		super.slash = File.separator;
	}

	/**
	 *
	 * 文件上传
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @param stream 文件输入流
	 * @return 文件绝对路径
	 */
	@Override
	public String upload(InputStream stream, String relativePath) throws IOException {
		// 目标文件
		final File file = new File(this.parentDir, relativePath);

		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			throw new FileException("文件上传失败! 创建父级文件夹失败! 父级路径: " + file.getParentFile().getPath());
		}

		if (!file.exists() && !file.createNewFile()) {
			throw new FileException("文件上传失败! 创建文件失败! 文件路径: " + file.getPath());
		}

		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			StreamUtils.write(stream, outputStream);
		}

		return relativePath;
	}

	/**
	 * 下载文件
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return java.io.FileOutputStream 文件流
	 */
	@Override
	public File download(String relativePath) {
		return new File(this.parentDir, relativePath);
	}

	/**
	 * 删除文件
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return boolean
	 */
	@Override
	public boolean delete(String relativePath) {
		final File file = new File(this.parentDir, relativePath);
		return file.exists() && file.delete();
	}

}
