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

package org.ballcat.common.util;

import java.io.IOException;
import java.io.InputStream;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
