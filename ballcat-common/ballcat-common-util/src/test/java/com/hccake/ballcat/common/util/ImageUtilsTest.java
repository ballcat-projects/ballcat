package com.hccake.ballcat.common.util;

import java.io.InputStream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2021/7/22 16:55
 */
class ImageUtilsTest {

	@SneakyThrows
	@Test
	void resolveClone() {
		final InputStream svg = FileUtils
				.getInputStreamByUrlPath("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
		final ImageUtils.ImageInfo info = ImageUtils.resolveClone(svg);
		Assertions.assertEquals("image/png", info.getType());
	}

	@SneakyThrows
	@Test
	void quickResolveClone() {
		final InputStream svg = FileUtils
				.getInputStreamByUrlPath("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
		final ImageUtils.ImageInfo info = ImageUtils.quickResolveClone(svg);
		Assertions.assertEquals("image/png", info.getType());
	}

}
