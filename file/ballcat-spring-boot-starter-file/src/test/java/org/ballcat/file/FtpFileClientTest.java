/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.ballcat.common.util.StreamUtils;
import org.ballcat.file.FileProperties.FtpProperties;
import org.ballcat.file.exception.FileException;
import org.ballcat.file.ftp.FtpFileClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author lingting 2021/10/18 17:16
 * @author 疯狂的狮子Li 2022-04-24
 */
@Disabled("Test only when necessary!")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FtpFileClientTest {

	private static FtpFileClient client;

	private static final String FILE_NAME = "test.txt";

	private static final String RELATIVE_PATH = "/" + FILE_NAME;

	private static final ClassLoader CLASS_LOADER = FtpFileClientTest.class.getClassLoader();

	private static final File OPERATE_FILE = new File(
			Objects.requireNonNull(CLASS_LOADER.getResource(FILE_NAME)).getFile());

	@BeforeAll
	static void init() throws Exception {
		String ftpUser = "ftpUser";
		String ftpPass = "ftpPass";
		int port = 59527;

		final String home = Paths.get("src", "test", "resources", "ftp").toAbsolutePath().toString();

		// 运行临时的 ftpServer
		BaseUser user = new BaseUser();
		user.setName(ftpUser);
		user.setPassword(ftpPass);
		user.setHomeDirectory(home);
		List<Authority> authorities = new ArrayList<>();
		authorities.add(new WritePermission());
		user.setAuthorities(authorities);

		ListenerFactory listenerFactory = new ListenerFactory();
		listenerFactory.setPort(port);
		FtpServerFactory serverFactory = new FtpServerFactory();
		serverFactory.getUserManager().save(user);

		serverFactory.addListener("default", listenerFactory.createListener());
		serverFactory.createServer().start();

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
		// Assertions.assertEquals(file.getName(), OPERATE_FILE.getName(), "文件名不匹配!");
		Assertions.assertEquals(StreamUtils.toString(Files.newInputStream(file.toPath())),
				StreamUtils.toString(Files.newInputStream(OPERATE_FILE.toPath())), "文件内容不匹配!");
	}

	@Test
	@Order(3)
	void delete() throws IOException {
		Assertions.assertTrue(client.delete(RELATIVE_PATH), "删除失败. 有问题");
		Assertions.assertThrows(FileException.class, () -> {
			client.delete(RELATIVE_PATH);
		});
	}

}
