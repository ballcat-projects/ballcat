package com.hccake.ballcat.common.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lingting 2021/7/22 16:55
 */
class ImageUtilsTest {

	@SneakyThrows(IOException.class)
	@Test
	void resolveClone() {
		InputStream svg = FileUtils
			.getInputStreamByUrlPath("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
		ImageUtils.ImageInfo info = ImageUtils.resolveClone(svg);
		Assertions.assertEquals("image/png", info.getType());
	}

	@SneakyThrows(IOException.class)
	@Test
	void quickResolveClone() {
		final InputStream svg = FileUtils
			.getInputStreamByUrlPath("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
		final ImageUtils.ImageInfo info = ImageUtils.quickResolveClone(svg);
		Assertions.assertEquals("image/png", info.getType());
	}

}
