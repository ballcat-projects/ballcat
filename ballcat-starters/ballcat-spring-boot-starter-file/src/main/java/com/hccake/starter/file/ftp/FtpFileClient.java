package com.hccake.starter.file.ftp;

import com.hccake.ballcat.common.util.FileUtils;
import com.hccake.starter.file.FileClient;
import com.hccake.starter.file.FileProperties.FtpProperties;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lingting 2021/10/17 20:11
 */
public class FtpFileClient implements FileClient {

	public static final String SLASH = "/";

	private final String rootPath;

	private final FTPClient client;

	public FtpFileClient(FtpProperties properties) throws IOException {
		client = new FTPClient();
		final FtpMode mode = properties.getMode();
		if (mode == FtpMode.ACTIVE) {
			client.enterLocalActiveMode();
		}
		else if (mode == FtpMode.PASSIVE) {
			client.enterLocalPassiveMode();
		}
		client.connect(properties.getIp(), properties.getPort());
		int replyCode = client.getReplyCode();
		if (!FTPReply.isPositiveCompletion(replyCode)) {
			throw new FtpFileException("ftp连接失败! 错误代码: " + replyCode);
		}

		client.login(properties.getUsername(), properties.getPassword());

		replyCode = client.getReplyCode();
		if (!FTPReply.isPositiveCompletion(replyCode)) {
			throw new FtpFileException("ftp登录失败! 错误代码: " + replyCode);
		}

		if (!StringUtils.hasText(properties.getPath())) {
			throw new NullPointerException("ftp文件根路径不能为空!");
		}
		client.setControlEncoding(properties.getEncoding());
		rootPath = properties.getPath().endsWith(SLASH) ? properties.getPath() : properties.getPath() + SLASH;
	}

	/**
	 * 获取操作的根路径
	 * @return java.lang.String
	 * @author lingting 2021-10-18 11:24
	 */
	@Override
	public String getRoot() {
		return rootPath;
	}

	/**
	 * 获取完整路径
	 * @param relativePath 文件相对 getRoot() 的路径@return java.lang.String
	 * @author lingting 2021-10-18 16:40
	 */
	@Override
	public String getWholePath(String relativePath) {
		if (relativePath.startsWith(SLASH)) {
			return getRoot() + relativePath.substring(1);
		}
		return getRoot() + relativePath;
	}

	/**
	 * 上传文件 - 不会关闭流. 请在成功后手动关闭
	 * @param stream 文件流
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return java.lang.String 文件完整路径
	 * @author lingting 2021-10-18 11:40
	 */
	@Override
	public String upload(InputStream stream, String relativePath) throws IOException {
		final String path = getWholePath(relativePath);

		// 上传失败
		if (!client.storeFile(path, stream)) {
			throw new FtpFileException(
					String.format("文件上传失败! 相对路径: %s; 根路径: %s; 请检查此路径是否存在以及登录用户是否拥有操作权限!", relativePath, path));
		}
		return path;
	}

	/**
	 * 下载文件
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return java.io.FileOutputStream 文件流
	 * @author lingting 2021-10-18 16:48
	 */
	@Override
	public File download(String relativePath) throws IOException {
		final String path = getWholePath(relativePath);
		// 临时文件
		final File tmpFile = FileUtils.getTemplateFile("ftpDownload");
		// 创建文件
		if (!tmpFile.createNewFile()) {
			throw new FtpFileException("文件下载失败! 临时文件生成失败!");
		}
		// 输出流
		try (FileOutputStream outputStream = new FileOutputStream(tmpFile)) {
			if (!client.retrieveFile(path, outputStream)) {
				throw new FtpFileException("文件下载失败! path: " + relativePath);
			}
		}
		return tmpFile;
	}

	/**
	 * 删除文件
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return boolean
	 * @author lingting 2021-10-18 17:14
	 */
	@Override
	public boolean delete(String relativePath) throws IOException {
		return client.deleteFile(getWholePath(relativePath));
	}

}
