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

package org.ballcat.tesseract;

import java.io.IOException;
import java.util.List;

import org.ballcat.tesseract.enums.TesseractLang;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting
 */
class TesseractCommandTest {

	static final TesseractImage image;

	static {
		TesseractImage image1;
		try {
			image1 = new TesseractImage("C:\\Users\\lingting\\Pictures\\1.jpg");
		}
		catch (IOException e) {
			image1 = null;
		}
		image = image1;
	}

	@Test
	void string() {
		final TesseractCommand.TesseractCommandBuilder builder = TesseractCommand.builder()
			.tesseract("tesseract")
			.lang(TesseractLang.EN.getVal())
			.image(image)
			.psm(3);

		final List<String> string = builder.build().run();
		Assertions.assertTrue(string.size() > 0);
		Assertions.assertEquals("10:56 AM", string.get(0));
	}

	@Test
	void boxes() {
		final TesseractCommand.TesseractCommandBuilder builder = TesseractCommand.builder()
			.tesseract("tesseract")
			.lang(TesseractLang.EN.getVal())
			.image(image)
			.boxes(true)
			.psm(3);

		final List<String> string = builder.build().run();
		Assertions.assertTrue(string.size() > 0);
		Assertions.assertEquals("1 14 1246 21 1262 0", string.get(0));
	}

}
