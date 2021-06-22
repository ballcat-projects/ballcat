package com.hccake.ballcat.file.service;

import com.hccake.ballcat.common.oss.OssClient;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author lingting 2021/5/27 11:14
 */
@Component
@RequiredArgsConstructor
public class FileService {

	private final OssClient ossClient;

	@SneakyThrows
	public String upload(InputStream stream, String relativePath, Long size) {
		String path;

		if (ossClient.enabled()) {
			path = ossClient.upload(stream, relativePath, size);
		}
		else {
			final File file = new File(relativePath);
			if (!file.exists()) {
				// 创建所需的父级目录. 可能已存在
				file.getParentFile().mkdirs();
				// 创建空文件
				if (!file.createNewFile()) {
					throw new BusinessException(BaseResultCode.FILE_UPLOAD_ERROR.getCode(), "文件创建失败!");
				}
				try (FileOutputStream fileStream = new FileOutputStream(file)) {
					byte[] line = new byte[2048];
					while (stream.read(line) > 0) {
						fileStream.write(line);
					}
				}
			}
			path = relativePath;
		}
		return path;
	}

}
