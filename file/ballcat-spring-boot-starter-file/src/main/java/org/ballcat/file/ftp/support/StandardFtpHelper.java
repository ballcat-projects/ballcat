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
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
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
public class StandardFtpHelper extends FtpHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(StandardFtpHelper.class);

	private FTPClient ftpClient;

	private final Set<String> sourceFiles = new HashSet<>();

	@Override
	public void loginFtpServer(String username, String password, String host, int port, Integer timeout, FtpMode mode)
			throws IOException {
		this.ftpClient = new FTPClient();
		try {
			// 连接
			this.ftpClient.connect(host, port);
			// 登录
			this.ftpClient.login(username, password);
			// 不需要写死ftp server的OS TYPE,FTPClient getSystemType()方法会自动识别
			/// ftpClient.configure(new FTPClientConfig(FTPClientConfig.SYST_UNIX));
			if (null != timeout) {
				this.ftpClient.setConnectTimeout(timeout);
				this.ftpClient.setDataTimeout(Duration.ofMillis(timeout));
			}
			if (mode == FtpMode.PASSIVE) {
				this.ftpClient.enterRemotePassiveMode();
				this.ftpClient.enterLocalPassiveMode();
			}
			else if (mode == FtpMode.ACTIVE) {
				this.ftpClient.enterLocalActiveMode();
				/// ftpClient.enterRemoteActiveMode(host, port);
			}
			int reply = this.ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				this.ftpClient.disconnect();
				String message = String.format("与ftp服务器建立连接失败,请检查用户名和密码是否正确: [%s]",
						"message:host =" + host + ",username = " + username + ",port =" + port);
				LOGGER.error(message);
				throw new FileException(message);
			}
			// 设置命令传输编码
			String fileEncoding = System.getProperty("file.encoding");
			this.ftpClient.setControlEncoding(fileEncoding);
		}
		catch (UnknownHostException e) {
			String message = String.format("请确认ftp服务器地址是否正确，无法连接到地址为: [%s] 的ftp服务器", host);
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
		catch (IllegalArgumentException e) {
			String message = String.format("请确认连接ftp服务器端口是否正确，错误的端口: [%s] ", port);
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
		catch (Exception e) {
			String message = String.format("与ftp服务器建立连接失败 : [%s]",
					"message:host =" + host + ",username = " + username + ",port =" + port);
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}

	}

	@Override
	public void logoutFtpServer() throws IOException {
		if (this.ftpClient.isConnected()) {
			try {
				// fixme ftpClient.completePendingCommand();//打开流操作之后必须，原因还需要深究
				this.ftpClient.logout();
			}
			catch (IOException e) {
				String message = "与ftp服务器断开连接失败";
				LOGGER.error(message, e);
				throw new FileException(message, e);
			}
		}
	}

	@Override
	public boolean isDirExist(String directoryPath) throws IOException {
		try {
			return this.ftpClient
				.changeWorkingDirectory(new String(directoryPath.getBytes(), FTP.DEFAULT_CONTROL_ENCODING));
		}
		catch (IOException e) {
			String message = String.format("进入目录：[%s]时发生I/O异常,请确认与ftp服务器的连接正常", directoryPath);
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
	}

	@Override
	public boolean isFileExist(String filePath) throws IOException {
		boolean isExitFlag = false;
		try {
			FTPFile[] ftpFiles = this.ftpClient
				.listFiles(new String(filePath.getBytes(), FTP.DEFAULT_CONTROL_ENCODING));
			if (ftpFiles.length == 1 && ftpFiles[0].isFile()) {
				isExitFlag = true;
			}
		}
		catch (IOException e) {
			String message = String.format("获取文件：[%s] 属性时发生I/O异常,请确认与ftp服务器的连接正常", filePath);
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
		return isExitFlag;
	}

	@Override
	public boolean isSymbolicLink(String filePath) throws IOException {
		boolean isExitFlag = false;
		try {
			FTPFile[] ftpFiles = this.ftpClient
				.listFiles(new String(filePath.getBytes(), FTP.DEFAULT_CONTROL_ENCODING));
			if (ftpFiles.length == 1 && ftpFiles[0].isSymbolicLink()) {
				isExitFlag = true;
			}
		}
		catch (IOException e) {
			String message = String.format("获取文件：[%s] 属性时发生I/O异常,请确认与ftp服务器的连接正常", filePath);
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
		return isExitFlag;
	}

	@Override
	public Set<String> getAllFilesInDir(String directoryPath, int parentLevel, int maxTraversalLevel)
			throws IOException {
		if (parentLevel < maxTraversalLevel) {
			// 父级目录,以'/'结尾
			String parentPath;
			int pathLen = directoryPath.length();
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
			else if (isFileExist(directoryPath)) {
				// path指向具体文件
				this.sourceFiles.add(directoryPath);
				return this.sourceFiles;
			}
			else if (isSymbolicLink(directoryPath)) {
				// path是链接文件
				String message = String.format("文件:[%s]是链接文件，当前不支持链接文件的读取", directoryPath);
				LOGGER.error(message);
				throw new FileException(message);
			}
			else {
				String message = String.format(CONFIG_PATH_MISSING_ERROR, directoryPath);
				LOGGER.error(message);
				throw new FileException(message);
			}

			try {
				FTPFile[] fs = this.ftpClient
					.listFiles(new String(directoryPath.getBytes(), FTP.DEFAULT_CONTROL_ENCODING));
				for (FTPFile ff : fs) {
					String strName = ff.getName();
					String filePath = parentPath + strName;
					if (ff.isDirectory()) {
						if (!(strName.equals(Symbol.DOT) || strName.equals(Symbol.DOUBLE_DOT))) {
							// 递归处理
							getAllFilesInDir(filePath, parentLevel + 1, maxTraversalLevel);
						}
					}
					else if (ff.isFile()) {
						// 是文件
						this.sourceFiles.add(filePath);
					}
					else if (ff.isSymbolicLink()) {
						// 是链接文件
						String message = String.format("文件:[%s]是链接文件，当前不支持链接文件的读取", filePath);
						LOGGER.error(message);
						throw new FileException(message);
					}
					else {
						String message = String.format("请确认path:[%s]存在，且配置的用户有权限读取", filePath);
						LOGGER.error(message);
						throw new FileException(message);
					}
				}
			}
			catch (IOException e) {
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
			return this.ftpClient.retrieveFileStream(new String(filePath.getBytes(), FTP.DEFAULT_CONTROL_ENCODING));
		}
		catch (IOException e) {
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
			this.ftpClient.changeWorkingDirectory(parentDir);
			this.printWorkingDirectory();
			OutputStream writeOutputStream = this.ftpClient.appendFileStream(filePath);
			String message = String.format("打开FTP文件[%s]获取写出流时出错,请确认文件%s有权限创建，有权限写出等", filePath, filePath);
			if (null == writeOutputStream) {
				throw new FileException(message);
			}

			return writeOutputStream;
		}
		catch (IOException e) {
			String message = String.format("写出文件 : [%s] 时出错,请确认文件:[%s]存在且配置的用户有权限写, errorMessage:%s", filePath,
					filePath, e.getMessage());
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
	}

	@Override
	public void mkdir(String directoryPath) throws IOException {
		String message = String.format("创建目录:%s时发生异常,请确认与ftp服务器的连接正常,拥有目录创建权限", directoryPath);
		try {
			this.printWorkingDirectory();
			boolean isDirExist = this.ftpClient.changeWorkingDirectory(directoryPath);
			if (!isDirExist) {
				int replayCode = this.ftpClient.mkd(directoryPath);
				message = String.format("%s,replayCode:%s", message, replayCode);
				if (replayCode != FTPReply.COMMAND_OK && replayCode != FTPReply.PATHNAME_CREATED) {
					throw new FileException(message);
				}
			}
		}
		catch (IOException e) {
			message = String.format("%s, errorMessage:%s", message, e.getMessage());
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
	}

	@Override
	public void mkDirRecursive(String directoryPath) throws IOException {
		StringBuilder dirPath = new StringBuilder();
		dirPath.append(Symbol.SLASH);
		String[] dirSplit = StringUtils.split(directoryPath, Symbol.SLASH);
		String message = String.format("创建目录:%s时发生异常,请确认与ftp服务器的连接正常,拥有目录创建权限", directoryPath);
		try {
			// ftp server不支持递归创建目录,只能一级一级创建
			for (String dirName : dirSplit) {
				dirPath.append(dirName);
				boolean mkdirSuccess = mkDirSingleHierarchy(dirPath.toString());
				dirPath.append(Symbol.SLASH);
				if (!mkdirSuccess) {
					throw new FileException(message);
				}
			}
		}
		catch (IOException e) {
			message = String.format("%s, errorMessage:%s", message, e.getMessage());
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
	}

	@Override
	public void completePendingCommand() throws IOException {
		/*
		 * Q:After I perform a file transfer to the server, printWorkingDirectory()
		 * returns null. A:You need to call completePendingCommand() after transferring
		 * the file. wiki: http://wiki.apache.org/commons/Net/FrequentlyAskedQuestions
		 */
		try {
			boolean isOk = this.ftpClient.completePendingCommand();
			if (!isOk) {
				throw new FileException("完成ftp completePendingCommand操作发生异常");
			}
		}
		catch (IOException e) {
			String message = String.format("完成ftp completePendingCommand操作发生异常, errorMessage:%s", e.getMessage());
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
	}

	@Override
	public void deleteFiles(Set<String> filesToDelete) throws IOException {
		String eachFile = null;
		boolean deleteOk;
		try {
			this.printWorkingDirectory();
			for (String each : filesToDelete) {
				LOGGER.info(String.format("delete file [%s].", each));
				eachFile = each;
				deleteOk = this.ftpClient.deleteFile(each);
				if (!deleteOk) {
					String message = String.format("删除文件:[%s] 时失败,请确认指定文件有删除权限", eachFile);
					throw new FileException(message);
				}
			}
		}
		catch (IOException e) {
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
				this.ftpClient.rename(each, targetPath);
			}
		}
		catch (IOException e) {
			String message = String.format("移动文件:[%s] 时发生异常,请确认指定文件有删除权限,以及网络交互正常, errorMessage:%s", eachFile,
					e.getMessage());
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
	}

	public boolean mkDirSingleHierarchy(String directoryPath) throws IOException {
		boolean isDirExist = this.ftpClient.changeWorkingDirectory(directoryPath);
		// 如果directoryPath目录不存在,则创建
		if (!isDirExist) {
			int replayCode = this.ftpClient.mkd(directoryPath);
			return replayCode == FTPReply.COMMAND_OK || replayCode == FTPReply.PATHNAME_CREATED;
		}
		return true;
	}

	private void printWorkingDirectory() {
		try {
			LOGGER.info(String.format("current working directory:%s", this.ftpClient.printWorkingDirectory()));
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
			this.ftpClient.changeWorkingDirectory(parentDir);
			this.printWorkingDirectory();
			this.ftpClient.retrieveFile(filePath, outputStream);
			return outputStream.toString();
		}
		catch (Exception e) {
			String message = String.format("读取文件 : [%s] 时出错,请确认文件:[%s]存在且配置的用户有权限读取, errorMessage:%s", filePath,
					filePath, e.getMessage());
			LOGGER.error(message, e);
			throw new FileException(message, e);
		}
	}

}
