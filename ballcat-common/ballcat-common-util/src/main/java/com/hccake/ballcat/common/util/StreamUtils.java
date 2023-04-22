package com.hccake.ballcat.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author lingting 2021/4/21 17:45
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StreamUtils {

	/**
	 * 默认大小 1024 * 1024 * 8
	 */
	public static final int DEFAULT_SIZE = 10485760;

	public static byte[] read(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		write(in, out);
		try {
			return out.toByteArray();
		}
		finally {
			close(out);
		}
	}

	public static void write(InputStream in, OutputStream out) throws IOException {
		write(in, out, DEFAULT_SIZE);
	}

	public static void write(InputStream in, OutputStream out, int size) throws IOException {
		byte[] bytes = new byte[size];
		int len;

		while (true) {
			len = in.read(bytes);
			if (len <= 0) {
				break;
			}

			out.write(bytes, 0, len);
		}
	}

	public static String toString(InputStream in) throws IOException {
		return toString(in, DEFAULT_SIZE, StandardCharsets.UTF_8);
	}

	public static String toString(InputStream in, int size, Charset charset) throws IOException {
		byte[] bytes = new byte[size];
		int len;

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		while (true) {
			len = in.read(bytes);
			if (len <= 0) {
				break;
			}
			outputStream.write(bytes, 0, len);
		}

		return outputStream.toString(charset.name());
	}

	/**
	 * 从流中读取 int
	 * @author lingting 2021-07-22 14:54
	 */
	public static int readInt(InputStream is, int noOfBytes, boolean bigEndian) throws IOException {
		int ret = 0;
		int sv = bigEndian ? ((noOfBytes - 1) * 8) : 0;
		int cnt = bigEndian ? -8 : 8;
		for (int i = 0; i < noOfBytes; i++) {
			ret |= is.read() << sv;
			sv += cnt;
		}
		return ret;
	}

	public static void close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		}
		catch (Exception e) {
			//
		}
	}

	/**
	 * 克隆文件流
	 * <p color="red">
	 * 注意: 在使用后及时关闭复制流
	 * </p>
	 * @param stream 源流
	 * @param amounts 数量
	 * @return 返回指定数量的从源流复制出来的只读流
	 * @author lingting 2021-04-16 16:18
	 */
	public static InputStream[] clone(InputStream stream, Integer amounts) throws IOException {
		return clone(stream, amounts, DEFAULT_SIZE);
	}

	public static InputStream[] clone(InputStream stream, Integer amounts, int size) throws IOException {
		InputStream[] streams = new InputStream[amounts];
		File[] files = new File[amounts];
		FileOutputStream[] outs = new FileOutputStream[amounts];

		byte[] buffer = new byte[size < 1 ? DEFAULT_SIZE : size];
		int len;

		while ((len = stream.read(buffer)) > -1) {
			for (int i = 0, outsLength = outs.length; i < outsLength; i++) {
				FileOutputStream out = outs[i];
				if (out == null) {
					files[i] = FileUtils.createTemp("clone." + i + "." + System.currentTimeMillis());
					out = new FileOutputStream(files[i]);
					outs[i] = out;
				}
				out.write(buffer, 0, len);
			}
		}

		for (int i = 0; i < files.length; i++) {
			try {
				outs[i].close();
			}
			catch (IOException e) {
				//
			}

			streams[i] = Files.newInputStream(files[i].toPath());
		}

		return streams;
	}

}
