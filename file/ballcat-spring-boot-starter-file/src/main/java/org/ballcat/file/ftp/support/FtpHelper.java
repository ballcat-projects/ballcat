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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ballcat.common.constant.Symbol;
import org.ballcat.file.exception.FileException;
import org.ballcat.file.ftp.FtpMode;

/**
 * 参考 alibaba.datax.plugin.ftpreader
 *
 * @author datax
 */
public abstract class FtpHelper {

	static final String CONFIG_PATH_MISSING_ERROR = "请确认您的配置项path:[%s]存在，且配置的用户有权限读取";
	static final String NO_SUCH_FILE = "no such file";

	public static final int DEFAULT_TIMEOUT = 60000;

	public void loginFtpServer(String username, String password, String host, int port) throws IOException {
		loginFtpServer(username, password, host, port, null, null);
	}

	/**
	 * 与ftp服务器建立连接
	 * @param host 主机
	 * @param username 用户名
	 * @param password 密码
	 * @param port 端口
	 * @param timeout 超时
	 * @param mode 模式
	 */
	public abstract void loginFtpServer(String username, String password, String host, int port, Integer timeout,
			FtpMode mode) throws IOException;

	/**
	 * 断开与ftp服务器的连接
	 */
	public abstract void logoutFtpServer() throws IOException;

	/**
	 * 判断指定路径是否是目录
	 * @param directoryPath 指定路径
	 * @return boolean
	 */
	public abstract boolean isDirExist(String directoryPath) throws IOException;

	/**
	 * 判断指定路径是否是文件
	 * @param filePath 指定路径
	 * @return boolean
	 */
	public abstract boolean isFileExist(String filePath) throws IOException;

	/**
	 * 判断指定路径是否是软链接
	 * @param filePath 指定路径
	 * @return boolean
	 */
	public abstract boolean isSymbolicLink(String filePath) throws IOException;

	/**
	 * 递归获取指定路径下符合条件的所有文件绝对路径
	 * @param directoryPath 指定路径
	 * @param parentLevel 父目录的递归层数（首次为0）
	 * @param maxTraversalLevel 允许的最大递归层数
	 * @return HashSet
	 */
	public abstract Set<String> getAllFilesInDir(String directoryPath, int parentLevel, int maxTraversalLevel)
			throws IOException;

	/**
	 * 获取指定路径的输入流（读文件）
	 * @param filePath 指定路径
	 * @return InputStream 流
	 */
	public abstract InputStream getInputStream(String filePath) throws IOException;

	/**
	 * 写入指定路径的输出流（写文件）
	 * @param filePath 指定路径
	 * @return InputStream
	 */
	public abstract OutputStream getOutputStream(String filePath) throws IOException;

	/**
	 * 获取指定路径列表下符合条件的所有文件的绝对路径
	 * @param srcPaths 路径列表
	 * @param parentLevel 父目录的递归层数（首次为0）
	 * @param maxTraversalLevel 允许的最大递归层数
	 * @return HashSet
	 */
	public Set<String> getAllFilesInDir(List<String> srcPaths, int parentLevel, int maxTraversalLevel)
			throws IOException {
		HashSet<String> sourceAllFiles = new HashSet<>();
		if (!srcPaths.isEmpty()) {
			for (String eachPath : srcPaths) {
				sourceAllFiles.addAll(getAllFilesInDir(eachPath, parentLevel, maxTraversalLevel));
			}
		}
		return sourceAllFiles;
	}

	/**
	 * 创建远程目录 不支持递归创建, 比如 mkdir -p
	 * @param directoryPath 远程目录
	 */
	public abstract void mkdir(String directoryPath) throws IOException;

	/**
	 * 创建远程目录 支持目录递归创建
	 * @param directoryPath 远程目录
	 */
	public abstract void mkDirRecursive(String directoryPath) throws IOException;

	/**
	 * Q:After I perform a file transfer to the server, printWorkingDirectory() returns
	 * null. A:You need to call completePendingCommand() after transferring the file.
	 * <a href="http://wiki.apache.org/commons/Net/FrequentlyAskedQuestions">wiki</a>
	 */
	public abstract void completePendingCommand() throws IOException;

	/**
	 * 删除文件 warn: 不支持文件夹删除, 比如 rm -rf
	 * @param filesToDelete 待删除文件路径
	 */
	public abstract void deleteFiles(Set<String> filesToDelete) throws IOException;

	/**
	 * 移动文件 warn: 不支持文件夹删除, 比如 rm -rf
	 * @param filesToMove 待移动文件路径
	 * @param targetPath 目标文件路径
	 */
	public abstract void moveFiles(Set<String> filesToMove, String targetPath) throws IOException;

	/**
	 * 获取远程路径文件内容
	 * @param filePath 远程路径
	 * @return 内容
	 */
	public abstract String getRemoteFileContent(String filePath) throws IOException;

	/**
	 * 获取含有通配符路径的父目录，目前只支持在最后一级目录使用通配符*或者?. (API jcraft.jsch.ChannelSftp.ls(String path)
	 * <a href="http://epaul.github.io/jsch-documentation/javadoc/">函数限制</a>)
	 * @param regexPath 正则表达式目录
	 * @return String
	 */
	String getRegexPathParentPath(String regexPath) throws IOException {
		int lastDirSeparator = regexPath.lastIndexOf(File.separatorChar);
		String parentPath;
		parentPath = regexPath.substring(0, lastDirSeparator + 1);
		if (parentPath.contains(Symbol.ASTERISK) || parentPath.contains(Symbol.QUESTION)) {
			throw new FileException(String.format("配置项目path中：[%s]不合法，目前只支持在最后一级目录使用通配符*或者?", regexPath));
		}
		return parentPath;
	}

}
