package com.hccake.ballcat.file.service;

import com.hccake.ballcat.common.oss.OssClient;
import com.hccake.ballcat.common.util.SpringUtils;
import com.hccake.starter.file.FileClient;
import java.io.InputStream;
import lombok.SneakyThrows;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * @author lingting 2021/5/27 11:14
 */
@Component
@DependsOn("springUtils")
public class FileService {

	private OssClient ossClient;

	private final FileClient fileClient;

	public FileService() {
		try {
			ossClient = SpringUtils.getBean(OssClient.class);
		}
		catch (Exception ignore) {
			ossClient = null;
		}

		// oss 为空或者未配置
		if (ossClient == null || !ossClient.isEnable()) {
			fileClient = SpringUtils.getBean(FileClient.class);
		}
		else {
			fileClient = null;
		}
	}

	@SneakyThrows
	public String upload(InputStream stream, String relativePath, Long size) {
		if (fileClient != null) {
			return fileClient.upload(stream, relativePath);
		}

		return ossClient.upload(stream, relativePath, size);
	}

}
