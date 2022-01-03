package com.hccake.ballcat.extend.tesseract;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting
 */
class TesseractTest {

	static final Tesseract tesseract = new Tesseract();

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
		final List<String> string = tesseract.toString(image);
		Assertions.assertEquals("10:56 AM", string.get(0));
	}

	@Test
	void boxes() {
		final List<TesseractBox> list = tesseract.toBoxes(image);
		Assertions.assertFalse(list.isEmpty());
		final TesseractBox boxes = list.get(0);
		Assertions.assertEquals("1", boxes.getText());
	}

}
