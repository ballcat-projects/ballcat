package com.ballcat.startes.oss.test;

import com.hccake.ballcat.common.oss.OssClient;
import com.hccake.ballcat.common.oss.OssProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2021/5/13 14:09
 */
@Slf4j
class OssTest {

	private OssProperties properties;

	private OssClient client;

	/**
	 * 阿里云
	 */
	@Test
	@SneakyThrows
	void ali() {

		properties = new OssProperties();
		// 所有操作(除方法上声明的外)都在此路径下进行
		properties.setObjectKeyPrefix("/");
		/*
		 * 如果本值不为空, 则在会进行文件上传时将文件的权限设置为该权限
		 *
		 * 如果值不为空请确认以下内容:
		 *
		 * 1. 您使用的access key 拥有修改文件权限 的权限
		 *
		 * 2. 您创建的桶允许文件被修改权限(如果不允许会有两种情况: 1. 请求报错,提示权限不足 2. 请求成功,文件正确上传,但是文件权限没有被修改)
		 */
		properties.setAcl(null);

		properties.setBucket("your bucket");
		properties.setAccessKey("your access key");
		properties.setAccessSecret("your access secret");

		// 根据自己的需求配置
		properties.setEndpoint("oss-cn-shanghai.aliyuncs.com");
		properties.setRegion("oss-cn-shanghai");

		client = new OssClient(properties);

		File file = new File("C:\\Users\\Administrator\\Desktop\\test.txt");
		InputStream stream = new FileInputStream(file);
		final String relativePath = "t.txt";
		client.upload(stream, relativePath);
		System.out.println(client.getDownloadUrl(relativePath));
		Assertions.assertNotNull(client.getDownloadUrl(relativePath));
	}

	@Test
	@SneakyThrows
	void aliDomain() {

		properties = new OssProperties();
		properties.setObjectKeyPrefix("/");
		properties.setAcl(null);

		properties.setBucket("your bucket");
		properties.setAccessKey("your access key");
		properties.setAccessSecret("your access secret");

		// 地区值不能为空
		properties.setRegion("region");
		properties.setDomain("http://ali.ballcat.com");

		client = new OssClient(properties);

		File file = new File("C:\\Users\\Administrator\\Desktop\\test.txt");
		InputStream stream = new FileInputStream(file);
		final String relativePath = "t.txt";
		client.upload(stream, relativePath);
		System.out.println(client.getDownloadUrl(relativePath));
		Assertions.assertNotNull(client.getDownloadUrl(relativePath));
	}

	/**
	 * 腾讯云
	 * <p>
	 * 腾讯云自定义域名没测, 搞不到测试用的域名
	 * </p>
	 */
	@Test
	@SneakyThrows
	void tx() {
		properties = new OssProperties();
		properties.setObjectKeyPrefix("/");
		properties.setAcl(null);

		properties.setBucket("your bucket");
		properties.setAccessKey("your secret id");
		properties.setAccessSecret("your secret key");
		// 根据自己的需求配置
		properties.setEndpoint("cos.ap-shanghai.myqcloud.com");
		properties.setRegion("cos.ap-shanghai");

		client = new OssClient(properties);

		File file = new File("C:\\Users\\Administrator\\Desktop\\test.txt");
		InputStream stream = new FileInputStream(file);
		final String relativePath = "t.txt";
		client.upload(stream, relativePath);
		System.out.println(client.getDownloadUrl(relativePath));
		Assertions.assertNotNull(client.getDownloadUrl(relativePath));
	}

	/**
	 * 亚马逊国际
	 */
	@Test
	@SneakyThrows
	void aws() {

		properties = new OssProperties();
		properties.setObjectKeyPrefix("/");
		properties.setAcl(null);

		properties.setBucket("your bucket");
		properties.setAccessKey("your access key");
		properties.setAccessSecret("your access secret");

		// 根据自己的需求配置
		properties.setEndpoint("ap-southeast-1.amazonaws.com");
		properties.setRegion("ap-southeast-1");

		client = new OssClient(properties);

		File file = new File("C:\\Users\\Administrator\\Desktop\\test.txt");
		InputStream stream = new FileInputStream(file);
		final String relativePath = "t.txt";
		client.upload(stream, relativePath);
		System.out.println(client.getDownloadUrl(relativePath));
		Assertions.assertNotNull(client.getDownloadUrl(relativePath));
	}

	@Test
	@SneakyThrows
	void awsDomain() {

		properties = new OssProperties();
		properties.setObjectKeyPrefix("/");
		properties.setAcl(null);

		properties.setBucket("your bucket");
		properties.setAccessKey("your access key");
		properties.setAccessSecret("your access secret");

		// 地区值不能为空
		properties.setRegion("ap-southeast-1");
		properties.setDomain("http://aws.ballcat.com");

		client = new OssClient(properties);

		File file = new File("C:\\Users\\Administrator\\Desktop\\test.txt");
		InputStream stream = new FileInputStream(file);
		final String relativePath = "t.txt";
		client.upload(stream, relativePath);
		System.out.println(client.getDownloadUrl(relativePath));
		Assertions.assertNotNull(client.getDownloadUrl(relativePath));
	}

}
