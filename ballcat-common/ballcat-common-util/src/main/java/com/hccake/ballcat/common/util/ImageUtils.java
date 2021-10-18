package com.hccake.ballcat.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2021/7/22 13:44
 */
@UtilityClass
public class ImageUtils {

	private static final List<ImageResolver> RESOLVER_LIST;

	static {
		RESOLVER_LIST = new ArrayList<>(16);
		// gif
		RESOLVER_LIST.add(new ImageResolver() {
			@Override
			public boolean isSupport(int r1, int r2, int r3) {
				return r1 == 'G' && r2 == 'I' && r3 == 'F';
			}

			@Override
			public void resolve(ImageInfo info, InputStream stream) throws IOException {
				stream.skip(3);
				info.setWidth(StreamUtils.readInt(stream, 2, false));
				info.setHeight(StreamUtils.readInt(stream, 2, false));
				info.setType("image/gif");
			}
		});
		// jpg
		RESOLVER_LIST.add(new ImageResolver() {
			@Override
			public boolean isSupport(int r1, int r2, int r3) {
				return r1 == 0xFF && r2 == 0xD8 && r3 == 255;
			}

			@Override
			public void resolve(ImageInfo info, InputStream stream) throws IOException {
				int r3;
				do {
					int marker = stream.read();
					int len = StreamUtils.readInt(stream, 2, true);
					if (marker == 192 || marker == 193 || marker == 194) {
						stream.skip(1);
						info.setHeight(StreamUtils.readInt(stream, 2, true));
						info.setWidth(StreamUtils.readInt(stream, 2, true));
						info.setType("image/jpeg");
						break;
					}
					stream.skip(len - 2);
					r3 = stream.read();
				}
				while (r3 == 255);
			}
		});
		// png
		RESOLVER_LIST.add(new ImageResolver() {
			@Override
			public boolean isSupport(int r1, int r2, int r3) {
				return r1 == 137 && r2 == 80 && r3 == 78;
			}

			@Override
			public void resolve(ImageInfo info, InputStream stream) throws IOException {
				stream.skip(15);
				info.setWidth(StreamUtils.readInt(stream, 2, true));
				stream.skip(2);
				info.setHeight(StreamUtils.readInt(stream, 2, true));
				info.setType("image/png");
			}
		});
		// bmp
		RESOLVER_LIST.add(new ImageResolver() {
			@Override
			public boolean isSupport(int r1, int r2, int r3) {
				return r1 == 66 && r2 == 77;
			}

			@Override
			public void resolve(ImageInfo info, InputStream stream) throws IOException {
				stream.skip(15);
				info.setWidth(StreamUtils.readInt(stream, 2, false));
				stream.skip(2);
				info.setHeight(StreamUtils.readInt(stream, 2, false));
				info.setType("image/bmp");
			}
		});

	}

	/**
	 * 注册解析方案
	 * @param resolver 解析方案
	 * @param index 所在位置. 越小越先执行, 最小值为0
	 * @author lingting 2021-07-22 16:50
	 */
	public void registerResolver(ImageResolver resolver, int index) {
		RESOLVER_LIST.add(Math.max(index, 0), resolver);
	}

	/**
	 * <h1>解析速度快, 但是如果图片格式不在预期内会无法解析</h1>
	 * <h2>图片格式不在预期内, 会抛出异常, 可以使用普通方法解析</h2>
	 * <p>
	 * 本方法会克隆传入的流, 并返回一个指针在起始位置的流对象
	 * </p>
	 * <p>
	 * 快速解析图片数据, 返回值携带可用的流
	 * </p>
	 * @throws IOException 流异常时抛出
	 * @author lingting 2021-07-22 14:09
	 */
	public ImageInfo quickResolveClone(InputStream stream) throws IOException {
		final InputStream[] streams = StreamUtils.clone(stream, 2);
		final ImageInfo info = new ImageInfo();
		// 返回可用的流
		info.setStream(streams[0]);
		stream.close();
		// 用于解析流
		stream = streams[1];

		int r1 = stream.read();
		int r2 = stream.read();
		int r3 = stream.read();
		for (ImageResolver resolver : RESOLVER_LIST) {
			if (resolver.isSupport(r1, r2, r3)) {
				resolver.resolve(info, stream);
				break;
			}
		}

		if (!StringUtils.hasText(info.getType())) {
			int r4 = stream.read();
			final boolean isTiff = (r1 == 'M' && r2 == 'M' && r3 == 0 && r4 == 42)
					|| (r1 == 'I' && r2 == 'I' && r3 == 42 && r4 == 0);
			// TIFF
			if (isTiff) {
				tiffResolver(stream, info, r1);
			}

		}

		stream.close();
		if (!StringUtils.hasText(info.getType())) {
			info.getStream().close();
			throw new IOException("未知图片类型");
		}
		return info;
	}

	/**
	 * <h1>解析图片 - 此方法会把图片直接载入内存, 谨慎处理大图片</h1>
	 * <p>
	 * 本方法会克隆传入的流, 并返回一个指针在起始位置的流对象
	 * </p>
	 * @param stream 流
	 * @return com.cloud.core.util.ImageUtils.ImageInfo
	 * @throws IOException 流异常时抛出
	 * @author lingting 2021-07-22 16:09
	 */
	public ImageInfo resolveClone(InputStream stream) throws IOException {
		final InputStream[] streams = StreamUtils.clone(stream, 2);
		final ImageInfo info = new ImageInfo();
		// 返回可用的流
		info.setStream(streams[0]);
		stream.close();
		// 用于解析流
		stream = streams[1];
		try {
			final ImageInputStream io = ImageIO.createImageInputStream(stream);
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(io);
			while (readers.hasNext()) {
				final ImageReader reader = readers.next();
				info.setType("image/" + reader.getFormatName().toLowerCase(Locale.ROOT));
				reader.setInput(io);
				info.setWidth(reader.getWidth(0));
				info.setHeight(reader.getHeight(0));
			}

			stream.close();
		}
		catch (Exception e) {
			info.getStream().close();
			throw e;
		}

		if (!StringUtils.hasText(info.getType())) {
			info.getStream().close();
			throw new IOException("未知图片类型");
		}

		return info;
	}

	/**
	 * <h1>混合解析, 先进去快速解析, 无结果再进行直接解析</h1>
	 * <h1>解析图片 - 此方法有可能会把图片直接载入内存, 谨慎处理大图片</h1>
	 * <p>
	 * 本方法会多次克隆传入的流, 并返回一个指针在起始位置的流对象
	 * </p>
	 * @param stream 流
	 * @return com.cloud.core.util.ImageUtils.ImageInfo
	 * @author lingting 2021-07-22 16:09
	 */
	public ImageInfo mixResolveClone(InputStream stream) throws IOException {
		InputStream[] streams = StreamUtils.clone(stream, 2);

		try {
			return quickResolveClone(streams[0]);
		}
		catch (Exception e) {
			return resolveClone(streams[1]);
		}

	}

	@Getter
	@Setter
	public static class ImageInfo {

		private ImageInfo() {
		}

		private InputStream stream;

		private long width;

		private long height;

		private String type;

	}

	public interface ImageResolver {

		/**
		 * 是否支持解析流
		 * @param r1 流1
		 * @param r2 流2
		 * @param r3 流3
		 * @return boolean
		 * @author lingting 2021-07-22 14:19
		 */
		boolean isSupport(int r1, int r2, int r3);

		/**
		 * 解析图片
		 * @param info 详情
		 * @param stream 图片流
		 * @throws IOException 流异常时抛出
		 * @author lingting 2021-07-22 14:51
		 */
		void resolve(ImageInfo info, InputStream stream) throws IOException;

	}

	/**
	 * tiff格式处理
	 * @author lingting 2021-07-22 16:28
	 */
	private void tiffResolver(InputStream stream, ImageInfo info, int r1) throws IOException {
		boolean bigEndian = r1 == 'M';
		int ifd = StreamUtils.readInt(stream, 4, bigEndian);
		int entries;
		stream.skip(ifd - 8);
		entries = StreamUtils.readInt(stream, 2, bigEndian);
		for (int i = 1; i <= entries; i++) {
			int tag = StreamUtils.readInt(stream, 2, bigEndian);
			int fieldType = StreamUtils.readInt(stream, 2, bigEndian);
			int valOffset;
			if ((fieldType == 3 || fieldType == 8)) {
				valOffset = StreamUtils.readInt(stream, 2, bigEndian);
				stream.skip(2);
			}
			else {
				valOffset = StreamUtils.readInt(stream, 4, bigEndian);
			}
			if (tag == 256) {
				info.setWidth(valOffset);
			}
			else if (tag == 257) {
				info.setHeight(valOffset);
			}
			if (info.getWidth() != -1 && info.getHeight() != -1) {
				info.setType("image/tiff");
				break;
			}
		}
	}

}
