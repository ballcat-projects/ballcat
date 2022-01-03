package com.hccake.ballcat.extend.tesseract;

import com.hccake.ballcat.extend.tesseract.enums.TesseractLang;
import java.io.IOException;
import java.util.List;
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
		final TesseractCommand.TesseractCommandBuilder builder = TesseractCommand.builder().tesseract("tesseract")
				.lang(TesseractLang.EN.getVal()).image(image).psm(3);

		final List<String> string = builder.build().run();
		Assertions.assertTrue(string.size() > 0);
		Assertions.assertEquals("10:56 AM", string.get(0));
	}

	@Test
	void boxes() {
		final TesseractCommand.TesseractCommandBuilder builder = TesseractCommand.builder().tesseract("tesseract")
				.lang(TesseractLang.EN.getVal()).image(image).boxes(true).psm(3);

		final List<String> string = builder.build().run();
		Assertions.assertTrue(string.size() > 0);
		Assertions.assertEquals("1 14 1246 21 1262 0", string.get(0));
	}

}
