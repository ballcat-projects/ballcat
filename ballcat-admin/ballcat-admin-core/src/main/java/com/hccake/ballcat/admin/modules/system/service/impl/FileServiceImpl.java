package com.hccake.ballcat.admin.modules.system.service.impl;

import com.hccake.ballcat.admin.modules.system.service.FileService;
import com.hccake.ballcat.commom.storage.FileStorageClient;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/1/8 11:16
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

	private final FileStorageClient client;

	/**
	 * 文件上传
	 * @param file 待上传文件
	 * @param objectName 文件对象名
	 *
	 */
	@Override
	public String uploadFile(MultipartFile file, String objectName) throws IOException {
		return client.upload(file.getInputStream(), objectName, file.getSize());
	}

	/**
	 * 文件上传
	 * @param inputStream 文件流
	 * @param objectName 文件对象名
	 *
	 */
	@SneakyThrows
	@Override
	public String uploadFile(InputStream inputStream, String objectName) {
		return client.upload(inputStream, objectName);
	}

}
