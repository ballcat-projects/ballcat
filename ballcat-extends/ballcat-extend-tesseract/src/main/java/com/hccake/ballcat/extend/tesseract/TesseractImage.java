package com.hccake.ballcat.extend.tesseract;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javax.imageio.ImageIO;
import lombok.Getter;

/**
 * @author lingting
 */
@Getter
public class TesseractImage {

	private static final File DIR = new File(System.getProperty("java.io.tmpdir"), "ballcat/tesseract");

	static {
		if (!DIR.exists()) {
			DIR.mkdir();
		}
	}

	/**
	 * 原始文件
	 */
	private final File rawFile;

	private final BufferedImage buffer;

	private final int width;

	private final int height;

	private final String type;

	private File tmpFile;

	public TesseractImage(String path) throws IOException {
		this(new File(path));
	}

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
		final BufferedImage grayBuffer = new BufferedImage(buffer.getWidth(), buffer.getHeight(), buffer.getType());

		for (int x = 0; x < buffer.getWidth(); x++) {
			for (int y = 0; y < buffer.getHeight(); y++) {
				final int rgb = buffer.getRGB(x, y);
				final int r = (rgb >> 16) & 0xff;
				final int g = (rgb >> 8) & 0xff;
				final int b = rgb & 0xff;
				int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);

				grayBuffer.setRGB(x, y, colorToRgb(gray, gray, gray));
			}
		}

		return new TesseractImage(rawFile, grayBuffer, type);
	}

	protected int colorToRgb(int red, int green, int blue) {
		int rgb = 255;
		rgb = rgb << 8;
		rgb += red;
		rgb = rgb << 8;
		rgb += green;
		rgb = rgb << 8;
		rgb += blue;
		return rgb;
	}

	/**
	 * 裁剪
	 * @return 裁剪后的图片
	 */
	public TesseractImage crop(Rectangle rectangle) {
		final BufferedImage cropBuffer = new BufferedImage(rectangle.width, rectangle.height, buffer.getType());

		for (int x = 0; x < buffer.getWidth(); x++) {
			for (int y = 0; y < buffer.getHeight(); y++) {
				if (rectangle.contains(x, y)) {
					cropBuffer.setRGB(x - rectangle.x, y - rectangle.y, buffer.getRGB(x, y));
				}
			}
		}

		return new TesseractImage(rawFile, cropBuffer, type);
	}

	/**
	 * 数据写入文件
	 * @return java.lang.String
	 */
	public String write() throws IOException {
		if (tmpFile == null) {
			tmpFile = new File(DIR, System.currentTimeMillis() + "." + type);
			if (tmpFile.createNewFile()) {
				ImageIO.write(buffer, type, tmpFile);
			}
			else {
				return write();
			}
		}
		return tmpFile.getPath();
	}

}
