package com.hccake.ballcat.file.service;

import com.hccake.ballcat.common.oss.OssClient;
import com.hccake.starter.file.core.FileClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lingting 2021/5/27 11:14
 */
@Component
public class FileService {

	private OssClient ossClient;

	private final FileClient fileClient;

	public FileService(ApplicationContext context) {
		try {
			ossClient = context.getBean(OssClient.class);
		}
		catch (Exception ignore) {
			ossClient = null;
		}

		// oss 为空或者未配置
		if (ossClient == null || !ossClient.isEnable()) {
			fileClient = context.getBean(FileClient.class);
		}
		else {
			fileClient = null;
		}
	}

	public String upload(InputStream stream, String relativePath, Long size) throws IOException {
		if (fileClient != null) {
			return fileClient.upload(stream, relativePath);
		}

		return ossClient.upload(stream, relativePath, size);
	}

}
