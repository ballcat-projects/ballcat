package com.hccake.ballcat.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
	 * 克隆文件流
	 * @param stream 源流
	 * @param amounts 数量
	 * @author lingting 2021-04-16 16:18
	 */
	public static InputStream[] clone(InputStream stream, Integer amounts) throws IOException {
		InputStream[] streams = new InputStream[amounts];
		ByteArrayOutputStream[] outs = new ByteArrayOutputStream[amounts];

		byte[] buffer = new byte[1024];
		int len;

		while ((len = stream.read(buffer)) > -1) {

			for (int i = 0, outsLength = outs.length; i < outsLength; i++) {
				ByteArrayOutputStream out = outs[i];
				if (out == null) {
					out = new ByteArrayOutputStream();
					outs[i] = out;
				}
				out.write(buffer, 0, len);
			}
		}

		for (int i = 0; i < outs.length; i++) {
			streams[i] = new ByteArrayInputStream(outs[i].toByteArray());
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
	public static void inputToOutput(InputStream inputStream, OutputStream outputStream) throws IOException {
		int read = 0;
		byte[] bytes = new byte[1024 * 1024];

		while ((read = inputStream.read(bytes)) > 0) {
			byte[] readBytes = new byte[read];
			System.arraycopy(bytes, 0, readBytes, 0, read);
			outputStream.write(readBytes);
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
