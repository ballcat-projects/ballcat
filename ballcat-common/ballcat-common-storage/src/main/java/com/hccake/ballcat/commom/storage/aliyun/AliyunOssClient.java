package com.hccake.ballcat.commom.storage.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.hccake.ballcat.commom.storage.FileStorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.InputStream;


/**
 * @author Hccake
 * @version 1.0
 * @date 2019/7/16 15:45
 */
@RequiredArgsConstructor
public class AliyunOssClient implements FileStorageClient, InitializingBean, DisposableBean {
	private final String endpoint;
	private final String accessKey;
	private final String accessSecret;
	private final String bucketName;

	private OSS client;


	/**
	 * 文件上传
	 *
	 * @param objectName  存储对象名称
	 * @param inputStream 文件输入流
	 * @return
	 */
	@Override
	public String putObject(String objectName, InputStream inputStream) {
		client.putObject(bucketName, objectName, inputStream);
		return objectName;
	}

	/**
	 * 文件删除
	 * @param objectName 存储对象名称
	 */
	@Override
	public void deleteObject(String objectName){
		if (client.doesObjectExist(bucketName, objectName)) {
			client.deleteObject(bucketName, objectName);
		}
	}

	/**
	 * 复制文件
	 * @param sourceObjectName 原对象名
	 * @param targetObjectName 目标对象名
	 */
	@Override
	public void copyObject(String sourceObjectName, String targetObjectName){
		client.copyObject(bucketName, sourceObjectName, bucketName, targetObjectName);
	}


	@Override
	public void afterPropertiesSet() {
		Assert.hasText(endpoint, "endpoint 为空");
		Assert.hasText(accessKey, "Oss accessKey为空");
		Assert.hasText(accessSecret, "Oss accessSecret为空");
		client = new OSSClientBuilder().build(endpoint, accessKey, accessSecret);
	}

	@Override
	public void destroy() {
		if (this.client != null) {
			this.client.shutdown();
		}
	}

}
