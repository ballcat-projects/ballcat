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

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

import lombok.Getter;

/**
 * <p>
 * TesseractImage class.
 * </p>
 *
 * @author lingting
 */
@Getter
public class TesseractImage {

	private static final File DIR = new File(System.getProperty("java.io.tmpdir"), "ballcat/tesseract");

	/**
	 * 原始文件
	 */
	private final File rawFile;

	private final BufferedImage buffer;

	private final int width;

	private final int height;

	private final String type;

	private File tmpFile;

	/**
	 * <p>
	 * Constructor for TesseractImage.
	 * </p>
	 * @param path a {@link java.lang.String} object.
	 * @throws java.io.IOException if any.
	 */
	public TesseractImage(String path) throws IOException {
		this(new File(path));
	}

	/**
	 * <p>
	 * Constructor for TesseractImage.
	 * </p>
	 * @param file a {@link java.io.File} object.
	 * @throws java.io.IOException if any.
	 */
	public TesseractImage(File file) throws IOException {
		this(file, ImageIO.read(file));
	}

	private TesseractImage(File rawFile, BufferedImage buffer) throws IOException {
		this(rawFile, buffer, ImageIO.getImageReaders(ImageIO.createImageInputStream(rawFile)).next().getFormatName());
	}

	private TesseractImage(File rawFile, BufferedImage buffer, String type) {
		this.rawFile = rawFile;
		this.buffer = buffer;
		this.width = buffer.getWidth();
		this.height = buffer.getHeight();
		this.type = type.toLowerCase(Locale.ROOT);
	}

	/**
	 * 转为灰度图 - 使用加权法
	 * @return 灰度图
	 */
	public TesseractImage rgb() {
		final BufferedImage grayBuffer = new BufferedImage(this.buffer.getWidth(), this.buffer.getHeight(),
				this.buffer.getType());

		for (int x = 0; x < this.buffer.getWidth(); x++) {
			for (int y = 0; y < this.buffer.getHeight(); y++) {
				grayBuffer.setRGB(x, y, toGrayRgb(this.buffer.getRGB(x, y)));
			}
		}

		return new TesseractImage(this.rawFile, grayBuffer, this.type);
	}

	/**
	 * 转灰度
	 * @param oldRgb 原颜色
	 * @return int 灰度颜色
	 */
	protected int toGrayRgb(int oldRgb) {
		final int r = (oldRgb >> 16) & 0xff;
		final int g = (oldRgb >> 8) & 0xff;
		final int b = oldRgb & 0xff;
		final int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);

		int rgb = 255 << 8;
		rgb += gray;
		rgb = rgb << 8;
		rgb += gray;
		rgb = rgb << 8;
		rgb += gray;
		return rgb;
	}

	/**
	 * 裁剪
	 * @return 裁剪后的图片
	 * @param rectangle a {@link java.awt.Rectangle} object.
	 */
	public TesseractImage crop(Rectangle rectangle) {
		final BufferedImage cropBuffer = new BufferedImage(rectangle.width, rectangle.height, this.buffer.getType());

		for (int x = 0; x < this.buffer.getWidth(); x++) {
			for (int y = 0; y < this.buffer.getHeight(); y++) {
				if (rectangle.contains(x, y)) {
					cropBuffer.setRGB(x - rectangle.x, y - rectangle.y, this.buffer.getRGB(x, y));
				}
			}
		}

		return new TesseractImage(this.rawFile, cropBuffer, this.type);
	}

	/**
	 * 数据写入文件
	 * @return java.lang.String
	 * @throws java.io.IOException if any.
	 */
	public String write() throws IOException {
		if (this.tmpFile == null) {
			if (!DIR.exists()) {
				DIR.mkdirs();
			}
			this.tmpFile = new File(DIR, System.currentTimeMillis() + "." + this.type);
			if (this.tmpFile.createNewFile()) {
				ImageIO.write(this.buffer, this.type, this.tmpFile);
			}
			else {
				return write();
			}
		}
		return this.tmpFile.getPath();
	}

}
