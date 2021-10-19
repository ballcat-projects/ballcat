package com.hccake.starter.file;

import com.hccake.ballcat.common.util.StreamUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

/**
 * @author lingting 2021/10/19 22:49
 */
class FileTest {

	private static FileClient client;

	private static final File OPERATE_FILE = new File(
			"C:\\Users\\lingting\\Documents\\Code\\React\\ballcat-ui-react\\package.json");

	@BeforeAll
	static void init() throws FileException {
		final FileProperties properties = new FileProperties();
		properties.setPath("");
		client = new FileClient(properties);
	}

	@Test
	void upload() throws IOException {
		final String upload = client.upload(new FileInputStream(OPERATE_FILE), "g2/g.json");
		System.out.println(upload);
	}

	@Test
	void download() throws IOException {
		final File file = client.download("g2/g.json");
		Assert.isTrue(StreamUtils.toString(new FileInputStream(file))
				.equals(StreamUtils.toString(new FileInputStream(OPERATE_FILE))), "文件内容不匹配!");
	}

	@Test
	void delete() throws IOException {
		Assert.isTrue(client.delete("g2/g.json"), "删除失败. 有问题");
		Assert.isTrue(!client.delete("g2/g.json"), "删除成功, 有问题");
	}

}
