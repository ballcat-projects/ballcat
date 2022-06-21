package com.hccake.ballcat.extend.tesseract;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

/**
 * @author lingting
 */
class TesseractImageTest {

	static final TesseractImage image;

	static {
		TesseractImage image1;
		try {
			image1 = new TesseractImage("C:\\Users\\lingting\\Pictures\\rgb.jpg");
		}
		catch (IOException e) {
			image1 = null;
		}
		image = image1;
	}

	@Test
	@SneakyThrows(IOException.class)
	void rgb() {
		final String write = image.rgb().write();
		System.out.println(write);
		Assertions.assertNotNull(write);
	}

	@Test
	@SneakyThrows(IOException.class)
	void crop() {
		final Rectangle rectangle = new Rectangle();
		rectangle.setLocation(238, 268);
		rectangle.setSize(100, 100);

		final String write = image.crop(rectangle).write();
		System.out.println(write);
		Assertions.assertNotNull(write);
	}

}
