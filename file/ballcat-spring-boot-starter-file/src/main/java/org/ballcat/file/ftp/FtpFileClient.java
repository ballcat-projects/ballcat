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

package org.ballcat.file.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Collections;

import org.ballcat.file.FileProperties.FtpProperties;
import org.ballcat.file.core.AbstractFileClient;
import org.ballcat.file.exception.FileException;
import org.ballcat.file.ftp.support.FtpHelper;
import org.ballcat.file.ftp.support.SftpHelper;
import org.ballcat.file.ftp.support.StandardFtpHelper;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2021/10/17 20:11
 * @author 疯狂的狮子Li 2022-04-24
 * @author evil0th 2023/6/6
 */
public class FtpFileClient extends AbstractFileClient {

	private final FtpHelper client;

	public FtpFileClient(FtpProperties properties) throws IOException {
		final FtpMode mode = properties.getMode();
		if (properties.isSftp()) {
			this.client = new SftpHelper();
		}
		else {
			this.client = new StandardFtpHelper();
		}
		this.client.loginFtpServer(properties.getUsername(), properties.getPassword(), properties.getIp(),
				properties.getPort(), null, mode);

		if (!StringUtils.hasText(properties.getPath())) {
			throw new NullPointerException("ftp文件根路径不能为空!");
		}

		super.rootPath = properties.getPath().endsWith(super.slash) ? properties.getPath()
				: properties.getPath() + super.slash;
	}

	/**
	 * 上传文件 - 不会关闭流. 请在成功后手动关闭
	 * @param stream 文件流
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return java.lang.String 文件完整路径
	 */
	@Override
	public String upload(InputStream stream, String relativePath) throws IOException {
		final String path = getWholePath(relativePath);
		try (OutputStream outputStream = this.client.getOutputStream(path)) {
			copy(stream, outputStream);
		}
		catch (IOException e) {
			throw new FileException(
					String.format("文件上传失败! 相对路径: %s; 根路径: %s; 请检查此路径是否存在以及登录用户是否拥有操作权限!", relativePath, path));
		}
		return path;
	}

	/**
	 * 下载文件
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return java.io.FileOutputStream 文件流
	 */
	@Override
	public File download(String relativePath) throws IOException {
		final String path = getWholePath(relativePath);
		// 临时文件 .tmp后缀
		File tmpFile = Files.createTempFile("", null).toFile();
		try (FileOutputStream outputStream = new FileOutputStream(tmpFile);
				InputStream inputStream = this.client.getInputStream(path)) {
			copy(inputStream, outputStream);
		}
		// JVM退出时删除临时文件
		// tmpFile.deleteOnExit();
		return tmpFile;
	}

	/**
	 * 删除文件
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return boolean
	 */
	@Override
	public boolean delete(String relativePath) throws IOException {
		final String path = getWholePath(relativePath);
		this.client.deleteFiles(Collections.singleton(path));
		return !this.client.isFileExist(path);
	}

	/**
	 * 流拷贝 FIXME: (by evil0th) 2023/6/7 JDK9+改为inputStream.transferTo(outputStream);
	 */
	private void copy(InputStream source, OutputStream target) throws IOException {
		byte[] buf = new byte[8192];
		int length;
		while ((length = source.read(buf)) != -1) {
			target.write(buf, 0, length);
		}
	}

}
