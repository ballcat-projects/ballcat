package com.hccake.ballcat.admin.modules.sys.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/1/8 11:15
 */
public interface FileService {

	/**
	 * 文件上传
	 * @param file 文件对象
	 * @param objectName 文件对象名
	 * @throws IOException IO异常
	 */
	void uploadFile(MultipartFile file, String objectName) throws IOException;

	/**
	 * 文件上传
	 * @param inputStream 文件流
	 * @param objectName 文件对象名
	 *
	 */
	void uploadFile(InputStream inputStream, String objectName);

}
