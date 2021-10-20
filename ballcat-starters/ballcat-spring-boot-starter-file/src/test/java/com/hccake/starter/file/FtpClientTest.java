package com.hccake.starter.file;

import com.hccake.ballcat.common.util.StreamUtils;
import com.hccake.starter.file.FileProperties.FtpProperties;
import com.hccake.starter.file.ftp.FtpClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

/**
 * @author lingting 2021/10/18 17:16
 */
class FtpClientTest {

	private static FtpClient client;

	private static final File OPERATE_FILE = new File(
			"C:\\Users\\lingting\\Documents\\Code\\React\\ballcat-ui-react\\package.json");

	@BeforeAll
	static void init() throws IOException {
		final FtpProperties properties = new FtpProperties();
		properties.setIp("ballcat-ftp");

		properties.setUsername("user");
		properties.setPassword("userpwd");
		client = new FtpClient(properties);
	}

	@Test
	void upload() throws IOException {
		final String uploadPath = client.upload(new FileInputStream(OPERATE_FILE), "package.json");
		Assert.isTrue(Objects.equals(uploadPath, "/package.json"), "结果路径异常!");
	}

	@Test
	void download() throws IOException {
		final File file = client.download("/package.json");
		Assert.isTrue(StreamUtils.toString(new FileInputStream(file))
				.equals(StreamUtils.toString(new FileInputStream(OPERATE_FILE))), "文件内容不匹配!");
	}

	@Test
	void delete() throws IOException {
		Assert.isTrue(client.delete("/package.json"), "删除失败. 有问题");
		Assert.isTrue(!client.delete("/package.json"), "删除成功, 有问题");
	}

}
