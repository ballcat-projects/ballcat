package com.hccake.starter.file;

import com.hccake.ballcat.common.util.StreamUtils;
import com.hccake.starter.file.local.LocalFileClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author lingting 2021/10/19 22:49
 * @author 疯狂的狮子Li 2022-04-24
 */
class LocalFileClientTest {

	private static LocalFileClient client;

	private static final File OPERATE_FILE = new File("D:\\th.jpg");

	@BeforeAll
	static void init() throws IOException {
		final FileProperties.LocalProperties localProperties = new FileProperties.LocalProperties();
		localProperties.setPath("D:\\user\\");
		client = new LocalFileClient(localProperties);
	}

	@Test
	void upload() throws IOException {
		final String upload = client.upload(Files.newInputStream(OPERATE_FILE.toPath()), "user/th.jpg");
		Assertions.assertEquals("user/th.jpg", upload);
	}

	@Test
	void download() throws IOException {
		final File file = client.download("user/th.jpg");
		Assertions.assertEquals(StreamUtils.toString(Files.newInputStream(file.toPath())),
				StreamUtils.toString(Files.newInputStream(OPERATE_FILE.toPath())), "文件内容不匹配!");
	}

	@Test
	void delete() throws IOException {
		Assertions.assertTrue(client.delete("user/th.jpg"), "删除失败. 有问题");
		Assertions.assertFalse(client.delete("user/th.jpg"), "删除成功, 有问题");
	}

}
