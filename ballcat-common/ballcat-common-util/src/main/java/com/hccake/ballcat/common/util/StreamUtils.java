package com.hccake.ballcat.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author lingting 2021/4/21 17:45
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StreamUtils {

	/**
	 * 默认大小 1024 * 1024 * 8
	 */
	public static final int DEFAULT_SIZE = 10485760;

	/**
	 * 克隆文件流
	 * @param stream 源流
	 * @param amounts 数量
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
					files[i] = FileUtils.getTemplateFile("clone." + i + "." + System.currentTimeMillis());
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

			streams[i] = new FileInputStream(files[i]);
		}

		return streams;
	}

	/**
	 * 流转字符串
	 * @author lingting 2021-04-29 18:07
	 */
	public static String toString(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		}
		return stringBuilder.toString();
	}

	/**
	 * 将输入流内容写入输出流
	 * @param inputStream 输入流
	 * @param outputStream 输出流
	 * @author lingting 2021-10-19 22:41
	 */
	public static void write(InputStream inputStream, OutputStream outputStream) throws IOException {
		write(inputStream, outputStream, DEFAULT_SIZE);
	}

	public static void write(InputStream inputStream, OutputStream outputStream, int size) throws IOException {
		int len;
		byte[] bytes = new byte[size < 1 ? DEFAULT_SIZE : size];

		while ((len = inputStream.read(bytes)) > 0) {
			outputStream.write(bytes, 0, len);
		}
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

}
