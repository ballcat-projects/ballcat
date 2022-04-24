package com.hccake.starter.file;

import cn.hutool.extra.ftp.SimpleFtpServer;
import com.hccake.ballcat.common.util.StreamUtils;
import com.hccake.starter.file.FileProperties.FtpProperties;
import com.hccake.starter.file.ftp.FtpFileClient;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingting 2021/10/18 17:16
 * @author 疯狂的狮子Li 2022-04-24
 */
class FtpFileClientTest {

	private static FtpFileClient client;

	private static final File OPERATE_FILE = new File("D:\\th.jpg");

	@BeforeAll
	static void init() throws IOException {
		final FtpProperties properties = new FtpProperties();
		properties.setIp("localhost");
		properties.setPort(21);
		properties.setUsername("lionli");
		properties.setPassword("666666");
		client = new FtpFileClient(properties);
	}

	@Test
	void upload() throws IOException {
		final String uploadPath = client.upload(Files.newInputStream(OPERATE_FILE.toPath()), "th.jpg");
		Assertions.assertEquals("/th.jpg", uploadPath, "结果路径异常!");
	}

	@Test
	void download() throws IOException {
		final File file = client.download("/th.jpg");
		Assertions.assertEquals(file.getName(), OPERATE_FILE.getName(), "文件名不匹配!");
		Assertions.assertEquals(StreamUtils.toString(Files.newInputStream(file.toPath())),
				StreamUtils.toString(Files.newInputStream(OPERATE_FILE.toPath())), "文件内容不匹配!");
	}

	@Test
	void delete() throws IOException {
		Assertions.assertTrue(client.delete("/th.jpg"), "删除失败. 有问题");
		Assertions.assertFalse(client.delete("/th.jpg"), "删除成功, 有问题");
	}

	public static void main(String[] args) {
		BaseUser user = new BaseUser();
		user.setName("lionli");
		user.setPassword("666666");
		user.setHomeDirectory("D:/user");
		List<Authority> authorities = new ArrayList<Authority>();
		authorities.add(new WritePermission());
		user.setAuthorities(authorities);

		SimpleFtpServer.create().setPort(21).addUser(user).start();
	}

}
