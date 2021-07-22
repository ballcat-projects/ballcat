package com.hccake.ballcat.common.util;

import java.io.File;
import java.io.FileInputStream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2021/7/22 16:55
 */
class ImageUtilsTest {

	final String path = "D:\\react\\ballcat-ui-react\\public\\icons\\icon-128x128.png";

	@SneakyThrows
	@Test
	void resolveClone() {
		File file = new File(path);
		final FileInputStream stream = new FileInputStream(file);
		final ImageUtils.ImageInfo info = ImageUtils.resolveClone(stream);
		System.out.println(info.getType());
		System.out.println(info.getWidth());
		System.out.println(info.getHeight());
	}

	@SneakyThrows
	@Test
	void quickResolveClone() {
		File file = new File(path);
		final FileInputStream stream = new FileInputStream(file);
		final ImageUtils.ImageInfo info = ImageUtils.quickResolveClone(stream);
		System.out.println(info.getType());
		System.out.println(info.getWidth());
		System.out.println(info.getHeight());
	}

}
