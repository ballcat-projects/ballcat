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

package org.ballcat.file.ftp.support;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import org.ballcat.common.constant.Symbol;
import org.ballcat.file.exception.FileException;
import org.ballcat.file.ftp.FtpMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * alibaba.datax.ftpreader
 *
 * @author datax
 */
public class SftpHelper extends FtpHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(SftpHelper.class);

	private Session session;

	private ChannelSftp channelSftp;

	private final Set<String> sourceFiles = new HashSet<>();

	@Override
	public void loginFtpServer(String username, String password, String host, int port, Integer timeout, FtpMode mode)
			throws IOException {
		JSch jsch = new JSch();
		try {
			this.session = jsch.getSession(username, host, port);
			// 根据用户名，主机ip，端口获取一个Session对象
			// 如果服务器连接不上，则抛出异常
			if (this.session == null) {
				throw new FileException("session is null,无法通过sftp与服务器建立链接，请检查主机名和用户名是否正确.");
			}
			// 设置密码
			this.session.setPassword(password);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			// 为Session对象设置properties
			this.session.setConfig(config);
			// 设置timeout时间
			if (null != timeout) {
				this.session.setTimeout(timeout);
			}
			// 通过Session建立链接
			this.session.connect();
			// 打开SFTP通道
			this.channelSftp = (ChannelSftp) this.session.openChannel("sftp");
			// 建立SFTP通道的连接
			this.channelSftp.connect();

			// 设置命令传输编码
			/// String fileEncoding = System.getProperty("file.encoding");
			// channelSftp.setFilenameEncoding(fileEncoding);
		}
		catch (JSchException e) {
			if (null != e.getCause()) {
				String cause = e.getCause().toString();
				String unknownHostException = "java.net.UnknownHostException: " + host;
				String illegalArgumentException = "java.lang.IllegalArgumentException: port out of range:" + port;
				String wrongPort = "java.net.ConnectException: Connection refused";
				if (unknownHostException.equals(cause)) {
					String message = String.format("请确认ftp服务器地址是否正确，无法连接到地址为: [%s] 的ftp服务器", host);
					LOGGER.error(message, e);
					throw new FileException(message, e);
				}
				else if (illegalArgumentException.equals(cause) || wrongPort.equals(cause)) {
					String message = String.format("请确认连接ftp服务器端口是否正确，错误的端口: [%s] ", port);
					LOGGER.error(message, e);
					throw new FileException(message, e);
				}
			}
			else {
				if ("Auth fail".equals(e.getMessage())) {
					String message = String.format("与ftp服务器建立连接失败,请检查用户名和密码是否正确: [%s]",
							"message:host =" + host + ",username = " + username + ",port =" + port);
					LOGGER.error(message, e);
					throw new FileException(message, e);
				}
				else {
					String message = String.format("与ftp服务器建立连接失败 : [%s]",
							"message:host =" + host + ",username = " + username + ",port =" + port);
					LOGGER.error(message, e);
					throw new FileException(message, e);
				}
			}
		}

	}

	@Override
	public void logoutFtpServer() {
		if (this.channelSftp != null) {
			this.channelSftp.disconnect();
		}
		if (this.session != null) {
			this.session.disconnect();
		}
	}

	@Override
	public boolean isDirExist(String directoryPath) throws IOException {
		try {
			SftpATTRS attrs = this.channelSftp.lstat(directoryPath);
			return attrs.isDir();
		}
		catch (SftpException e) {
			if (NO_SUCH_FILE.equalsIgnoreCase(e.getMessage())) {
				String message = String.format(CONFIG_PATH_MISSING_ERROR, directoryPath);
				LOGGER.error(message, e);
				throw new FileException(message, e);
			}
			String message = String.format("进入目录：[%s]时发生I/O异常,请确认与ftp服务器的连接正常", directoryPath);
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
	}

	@Override
	public boolean isFileExist(String filePath) throws IOException {
		boolean isExitFlag = false;
		try {
			SftpATTRS attrs = this.channelSftp.lstat(filePath);
			if (attrs.getSize() >= 0) {
				isExitFlag = true;
			}
		}
		catch (SftpException e) {
			if (NO_SUCH_FILE.equalsIgnoreCase(e.getMessage())) {
				String message = String.format(CONFIG_PATH_MISSING_ERROR, filePath);
				LOGGER.error(message, e);
				throw new FileException(message, e);
			}
			else {
				String message = String.format("获取文件：[%s] 属性时发生I/O异常,请确认与ftp服务器的连接正常", filePath);
				LOGGER.error(message, e);
				throw new FileException(message, e);
			}
		}
		return isExitFlag;
	}

	@Override
	public boolean isSymbolicLink(String filePath) throws IOException {
		try {
			SftpATTRS attrs = this.channelSftp.lstat(filePath);
			return attrs.isLink();
		}
		catch (SftpException e) {
			if (NO_SUCH_FILE.equalsIgnoreCase(e.getMessage())) {
				String message = String.format(CONFIG_PATH_MISSING_ERROR, filePath);
				LOGGER.error(message, e);
				throw new FileException(message, e);
			}
			else {
				String message = String.format("获取文件：[%s] 属性时发生I/O异常,请确认与ftp服务器的连接正常", filePath);
				LOGGER.error(message, e);
				throw new FileException(message, e);
			}
		}
	}

	@Override
	public Set<String> getAllFilesInDir(String directoryPath, int parentLevel, int maxTraversalLevel)
			throws IOException {
		if (parentLevel < maxTraversalLevel) {
			// 父级目录,以'/'结尾
			String parentPath;
			int pathLen = directoryPath.length();
			// *和？的限制
			if (directoryPath.contains(Symbol.ASTERISK) || directoryPath.contains(Symbol.QUESTION)) {
				// path是正则表达式
				String subPath = getRegexPathParentPath(directoryPath);
				if (isDirExist(subPath)) {
					parentPath = subPath;
				}
				else {
					String message = String.format("不能进入目录：[%s]," + "请确认您的配置项path:[%s]存在，且配置的用户有权限进入", subPath,
							directoryPath);
					LOGGER.error(message);
					throw new FileException(message);
				}

			}
			else if (isDirExist(directoryPath)) {
				// path是目录
				if (directoryPath.charAt(pathLen - 1) == File.separatorChar) {
					parentPath = directoryPath;
				}
				else {
					parentPath = directoryPath + File.separatorChar;
				}
			}
			else if (isSymbolicLink(directoryPath)) {
				// path是链接文件
				String message = String.format("文件:[%s]是链接文件，当前不支持链接文件的读取", directoryPath);
				LOGGER.error(message);
				throw new FileException(message);
			}
			else if (isFileExist(directoryPath)) {
				// path指向具体文件
				this.sourceFiles.add(directoryPath);
				return this.sourceFiles;
			}
			else {
				String message = String.format(CONFIG_PATH_MISSING_ERROR, directoryPath);
				LOGGER.error(message);
				throw new FileException(message);
			}
			try {
				Vector<?> files = this.channelSftp.ls(directoryPath);
				for (Object o : files) {
					ChannelSftp.LsEntry le = (ChannelSftp.LsEntry) o;
					String strName = le.getFilename();
					String filePath = parentPath + strName;
					if (isDirExist(filePath)) {
						// 是子目录
						if (!(strName.equals(Symbol.DOT) || strName.equals(Symbol.DOUBLE_DOT))) {
							// 递归处理
							getAllFilesInDir(filePath, parentLevel + 1, maxTraversalLevel);
						}
					}
					else if (isSymbolicLink(filePath)) {
						// 是链接文件
						String message = String.format("文件:[%s]是链接文件，当前不支持链接文件的读取", filePath);
						LOGGER.error(message);
						throw new FileException(message);
					}
					else if (isFileExist(filePath)) {
						// 是文件
						this.sourceFiles.add(filePath);
					}
					else {
						String message = String.format("请确认path:[%s]存在，且配置的用户有权限读取", filePath);
						LOGGER.error(message);
						throw new FileException(message);
					}
				}
			}
			catch (SftpException e) {
				String message = String.format("获取path：[%s] 下文件列表时发生I/O异常,请确认与ftp服务器的连接正常", directoryPath);
				LOGGER.error(message, e);
				throw new FileException(message, e);
			}
			return this.sourceFiles;
		}
		else {
			// 超出最大递归层数
			String message = String.format("获取path：[%s] 下文件列表时超出最大层数,请确认路径[%s]下不存在软连接文件", directoryPath, directoryPath);
			LOGGER.error(message);
			throw new FileException(message);
		}
	}

	@Override
	public InputStream getInputStream(String filePath) throws IOException {
		try {
			return this.channelSftp.get(filePath);
		}
		catch (SftpException e) {
			String message = String.format("读取文件 : [%s] 时出错,请确认文件：[%s]存在且配置的用户有权限读取", filePath, filePath);
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
	}

	@Override
	public OutputStream getOutputStream(String filePath) throws IOException {
		try {
			this.printWorkingDirectory();
			String parentDir = filePath.substring(0, filePath.lastIndexOf(File.separatorChar));
			this.channelSftp.cd(parentDir);
			this.printWorkingDirectory();
			OutputStream writeOutputStream = this.channelSftp.put(filePath, ChannelSftp.APPEND);
			String message = String.format("打开FTP文件[%s]获取写出流时出错,请确认文件%s有权限创建，有权限写出等", filePath, filePath);
			if (null == writeOutputStream) {
				throw new FileException(message);
			}
			return writeOutputStream;
		}
		catch (SftpException e) {
			String message = String.format("写出文件[%s] 时出错,请确认文件%s有权限写出, errorMessage:%s", filePath, filePath,
					e.getMessage());
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
	}

	@Override
	public void mkdir(String directoryPath) throws IOException {
		boolean isDirExist = false;
		try {
			this.printWorkingDirectory();
			SftpATTRS attrs = this.channelSftp.lstat(directoryPath);
			isDirExist = attrs.isDir();
		}
		catch (SftpException e) {
			if (NO_SUCH_FILE.equalsIgnoreCase(e.getMessage())) {
				LOGGER.warn(
						String.format("您的配置项path:[%s]不存在，将尝试进行目录创建, errorMessage:%s", directoryPath, e.getMessage()),
						e);
				isDirExist = false;
			}
		}
		if (!isDirExist) {
			try {
				// warn 检查mkdir -p
				this.channelSftp.mkdir(directoryPath);
			}
			catch (SftpException e) {
				String message = String.format("创建目录:%s时发生I/O异常,请确认与ftp服务器的连接正常,拥有目录创建权限, errorMessage:%s",
						directoryPath, e.getMessage());
				LOGGER.error(message, e);
				throw new FileException(message, e);
			}
		}
	}

	@Override
	public void mkDirRecursive(String directoryPath) throws IOException {
		boolean isDirExist = false;
		try {
			this.printWorkingDirectory();
			SftpATTRS attrs = this.channelSftp.lstat(directoryPath);
			isDirExist = attrs.isDir();
		}
		catch (SftpException e) {
			if (NO_SUCH_FILE.equalsIgnoreCase(e.getMessage())) {
				LOGGER.warn(
						String.format("您的配置项path:[%s]不存在，将尝试进行目录创建, errorMessage:%s", directoryPath, e.getMessage()),
						e);
			}
		}
		if (!isDirExist) {
			StringBuilder dirPath = new StringBuilder();
			dirPath.append(Symbol.SLASH);
			String[] dirSplit = StringUtils.split(directoryPath, Symbol.SLASH);
			try {
				// ftp server不支持递归创建目录,只能一级一级创建
				for (String dirName : dirSplit) {
					dirPath.append(dirName);
					mkDirSingleHierarchy(dirPath.toString());
					dirPath.append(Symbol.SLASH);
				}
			}
			catch (SftpException e) {
				String message = String.format("创建目录:%s时发生I/O异常,请确认与ftp服务器的连接正常,拥有目录创建权限, errorMessage:%s",
						directoryPath, e.getMessage());
				LOGGER.error(message, e);
				throw new FileException(message, e);
			}
		}
	}

	@Override
	public void completePendingCommand() {
		// un
	}

	@Override
	public void deleteFiles(Set<String> filesToDelete) throws IOException {
		String eachFile = null;
		try {
			this.printWorkingDirectory();
			for (String each : filesToDelete) {
				LOGGER.info(String.format("delete file [%s].", each));
				eachFile = each;
				this.channelSftp.rm(each);
			}
		}
		catch (SftpException e) {
			String message = String.format("删除文件:[%s] 时发生异常,请确认指定文件有删除权限,以及网络交互正常, errorMessage:%s", eachFile,
					e.getMessage());
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
	}

	@Override
	public void moveFiles(Set<String> filesToMove, String targetPath) throws IOException {
		if (!StringUtils.hasText(targetPath)) {
			LOGGER.error("目标目录路径为空!");
			throw new FileException("目标目录路径为空!");
		}
		String eachFile = null;
		try {
			this.printWorkingDirectory();
			// 创建目录
			mkdir(targetPath);
			for (String each : filesToMove) {
				eachFile = each;
				String targetName = String.format("%s%s", targetPath.endsWith(File.separator)
						? targetPath.substring(0, targetPath.length() - 1) : targetPath,
						each.substring(each.lastIndexOf(File.separator)));
				LOGGER.info(String.format("rename file [%s] to [%s] .", each, targetName));
				this.channelSftp.rename(each, targetName);
			}
		}
		catch (SftpException e) {
			String message = String.format("移动文件:[%s] 时发生异常,请确认指定文件有删除权限,以及网络交互正常, errorMessage:%s", eachFile,
					e.getMessage());
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
	}

	public boolean mkDirSingleHierarchy(String directoryPath) throws SftpException {
		boolean isDirExist = false;
		try {
			SftpATTRS attrs = this.channelSftp.lstat(directoryPath);
			isDirExist = attrs.isDir();
		}
		catch (SftpException e) {
			if (!isDirExist) {
				LOGGER.info(String.format("正在逐级创建目录 [%s]", directoryPath));
				this.channelSftp.mkdir(directoryPath);
				return true;
			}
		}
		if (!isDirExist) {
			LOGGER.info(String.format("正在逐级创建目录 [%s]", directoryPath));
			this.channelSftp.mkdir(directoryPath);
		}
		return true;
	}

	private void printWorkingDirectory() {
		try {
			LOGGER.info(String.format("current working directory:%s", this.channelSftp.pwd()));
		}
		catch (Exception e) {
			LOGGER.warn(String.format("printWorkingDirectory error:%s", e.getMessage()));
		}
	}

	@Override
	public String getRemoteFileContent(String filePath) throws IOException {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(22)) {
			this.completePendingCommand();
			this.printWorkingDirectory();
			String parentDir = filePath.substring(0, filePath.lastIndexOf(File.separatorChar));
			this.channelSftp.cd(parentDir);
			this.printWorkingDirectory();
			this.channelSftp.get(filePath, outputStream);
			return outputStream.toString();
		}
		catch (SftpException e) {
			String message = String.format("写出文件[%s] 时出错,请确认文件%s有权限写出, errorMessage:%s", filePath, filePath,
					e.getMessage());
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
	}

}
