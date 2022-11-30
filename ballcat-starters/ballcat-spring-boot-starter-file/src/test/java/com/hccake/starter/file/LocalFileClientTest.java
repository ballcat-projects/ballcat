package com.hccake.starter.file;

import com.hccake.ballcat.common.util.StreamUtils;
import com.hccake.starter.file.local.LocalFileClient;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * @author lingting 2021/10/19 22:49
 * @author 疯狂的狮子Li 2022-04-24
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LocalFileClientTest {

	private static LocalFileClient client;

	private static final String FILE_NAME = "test.txt";

	private static final String UPLOADED_FILE_RELATIVE_PATH = "/" + FILE_NAME;

	private static final ClassLoader CLASS_LOADER = FtpFileClientTest.class.getClassLoader();

	private static final File OPERATE_FILE = new File(
			Objects.requireNonNull(CLASS_LOADER.getResource(FILE_NAME)).getFile());

	@BeforeAll
	static void init() throws IOException {
		final FileProperties.LocalProperties localProperties = new FileProperties.LocalProperties();
		localProperties.setPath(Objects.requireNonNull(CLASS_LOADER.getResource("./")).getPath() + "local");
		client = new LocalFileClient(localProperties);
	}

	@Test
	@Order(1)
	void upload() throws IOException {
		final String upload = client.upload(Files.newInputStream(OPERATE_FILE.toPath()), UPLOADED_FILE_RELATIVE_PATH);
		Assertions.assertEquals(UPLOADED_FILE_RELATIVE_PATH, upload);
	}

	@Test
	@Order(2)
	void download() throws IOException {
		final File file = client.download(UPLOADED_FILE_RELATIVE_PATH);
		Assertions.assertEquals(StreamUtils.toString(Files.newInputStream(file.toPath())),
				StreamUtils.toString(Files.newInputStream(OPERATE_FILE.toPath())), "文件内容不匹配!");
	}

	@Test
	@Order(3)
	void delete() throws IOException {
		Assertions.assertTrue(client.delete(UPLOADED_FILE_RELATIVE_PATH), "删除失败. 有问题");
		Assertions.assertFalse(client.delete(UPLOADED_FILE_RELATIVE_PATH), "删除成功, 有问题");
	}

}
