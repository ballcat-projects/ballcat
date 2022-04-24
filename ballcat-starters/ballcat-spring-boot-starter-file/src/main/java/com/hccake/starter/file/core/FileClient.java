package com.hccake.starter.file.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lingting 2021/10/17 20:11
 * @author 疯狂的狮子Li 2022-04-24
 */
public interface FileClient {

	/**
	 *
	 * 文件上传
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @param stream 文件输入流
	 * @return 文件绝对路径
	 * @author lingting 2021-10-19 22:32
	 */
	String upload(InputStream stream, String relativePath) throws IOException;

	/**
	 * 下载文件
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return java.io.FileOutputStream 文件流
	 * @author lingting 2021-10-18 16:48
	 */
	File download(String relativePath) throws IOException;

	/**
	 * 删除文件
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return boolean
	 * @author lingting 2021-10-18 17:14
	 */
	boolean delete(String relativePath) throws IOException;

}
