package com.hccake.starter.file;

import cn.hutool.extra.ftp.SimpleFtpServer;
import com.hccake.ballcat.common.util.StreamUtils;
import com.hccake.starter.file.FileProperties.FtpProperties;
import com.hccake.starter.file.ftp.FtpFileClient;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lingting 2021/10/18 17:16
 * @author 疯狂的狮子Li 2022-04-24
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FtpFileClientTest {

	private static FtpFileClient client;

	private static final String FILE_NAME = "test.txt";

	private static final String RELATIVE_PATH = "/" + FILE_NAME;

	private static final ClassLoader CLASS_LOADER = FtpFileClientTest.class.getClassLoader();

	private static final File OPERATE_FILE = new File(
			Objects.requireNonNull(CLASS_LOADER.getResource(FILE_NAME)).getFile());

	@BeforeAll
	static void init() {
		String ftpUser = "ftpUser";
		String ftpPass = "ftpPass";
		int port = 9999;

		// 运行临时的 ftpServer
		BaseUser user = new BaseUser();
		user.setName(ftpUser);
		user.setPassword(ftpPass);
		user.setHomeDirectory(Objects.requireNonNull(CLASS_LOADER.getResource("./")).getPath() + "ftp");
		List<Authority> authorities = new ArrayList<>();
		authorities.add(new WritePermission());
		user.setAuthorities(authorities);
		SimpleFtpServer.create().setPort(port).addUser(user).start();

		final FtpProperties properties = new FtpProperties();
		properties.setIp("localhost");
		properties.setPort(port);
		properties.setUsername(ftpUser);
		properties.setPassword(ftpPass);
		client = new FtpFileClient(properties);
	}

	@Test
	@Order(1)
	void upload() throws IOException {
		final String uploadPath = client.upload(Files.newInputStream(OPERATE_FILE.toPath()), FILE_NAME);
		Assertions.assertEquals(RELATIVE_PATH, uploadPath, "结果路径异常!");
	}

	@Test
	@Order(2)
	void download() throws IOException {
		final File file = client.download(RELATIVE_PATH);
		Assertions.assertEquals(file.getName(), OPERATE_FILE.getName(), "文件名不匹配!");
		Assertions.assertEquals(StreamUtils.toString(Files.newInputStream(file.toPath())),
				StreamUtils.toString(Files.newInputStream(OPERATE_FILE.toPath())), "文件内容不匹配!");
	}

	@Test
	@Order(3)
	void delete() throws IOException {
		Assertions.assertTrue(client.delete(RELATIVE_PATH), "删除失败. 有问题");
		Assertions.assertFalse(client.delete(RELATIVE_PATH), "删除成功, 有问题");
	}

}
